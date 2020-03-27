//package com.platform.top.xiaoyu.run.service.finance.service.impl;
//
//import com.platform.top.xiaoyu.run.api.operate.feign.InAndOutOfStyleSettingFeignClient;
//import com.platform.top.xiaoyu.run.api.operate.vo.req.InAndOutOfStyleQueReq;
//import com.platform.top.xiaoyu.run.api.operate.vo.resp.InAndOutOfStyleSettingResp;
//import com.platform.top.xiaoyu.run.service.api.common.enums.EnumSetTheType;
//import com.platform.top.xiaoyu.run.service.api.es.stream.BetMoneyCodeParent;
//import com.platform.top.xiaoyu.run.service.api.finance.enums.BusStatusEnums;
//import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
//import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
//import com.platform.top.xiaoyu.run.service.api.finance.vo.OrderSynVO;
//import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
//import com.platform.top.xiaoyu.run.service.finance.entity.OrderSyn;
//import com.platform.top.xiaoyu.run.service.finance.entity.OrderSynData;
//import com.platform.top.xiaoyu.run.service.finance.service.*;
//import com.top.xiaoyu.rearend.tool.api.R;
//import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
//import com.top.xiaoyu.rearend.tool.idx.IdService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 洗码订单 服务实现类
// * @author xiaoyu
// */
//@Service
//@Slf4j
//public class OrderServiceImpl implements IOrderService {
//
//	@Autowired
//	private IBusRechargeService rechargeService;
//	@Autowired
//	private IOrderSynDataService orderSynDataService;
//	@Autowired
//	private InAndOutOfStyleSettingFeignClient inAndOutOfStyleSettingFeignClient;
//	@Autowired
//	private IBusinessService businessService;
//	@Autowired
//	private IOrderSynService orderSynService;
//	@Autowired
//	private IdService idService;
//
//	/**
//	 * 查询当前用户所有充值记录
//	 * RPC 查询打码配置
//	 * 实际打码（订单投注额） >= 需求代码 * 配置倍数
//	 * @param userId 用户ID
//	 * @param platformId 平台ID
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void calc(Long userId, Long platformId, String amountCount, List<BetMoneyCodeParent.BetMoneyCodeMsg> listOrder) {
//
//		//查询充值记录，未满足 打码金额的所有数据。 创建时间 降序查询
//		List<BusinessVO> listBusVO = rechargeService.findListAll(BusinessVO.builder().userId(userId).platformId(platformId).build());
//		if(CollectionUtils.isEmpty(listBusVO)) {
//			log.debug("未查询到充值数据");
//			return;
//		}
//
//		//充值时间， 距离现在最久一条充值数据
//		LocalDateTime beginDateTime = listBusVO.get(listBusVO.size()-1).getCreateTimestamp();
//
//		//查询上次最近的订单时间， 抓取剩余打码金额
//		OrderSynVO orderSynVO = orderSynService.findLastDetail(userId, platformId);
//		if( null != orderSynVO) {
//			if( beginDateTime.isAfter(orderSynVO.getLastOrderDatetime()) ) {
//				//充值时间不能大于 最后一次更新同步的时间； 每次同步订单后的打码量满足充值需求打码量，则更新充值数据为 满足
//				log.info("充值时间不能大于 最后一次更新同步的时间");
//				throw new BizBusinessException(BaseExceptionType.ORDERSYN_DATA_FAIL);
//			}
//		}
//
//		//配置的打码倍数
////		BigDecimal toolsRate = new BigDecimal("1");
//		BigDecimal toolsRate = this.getTools(platformId);
//		if(null == toolsRate || toolsRate.longValue() <= 0 ) {
//			log.info("RPC查询到 打码配置数据， service 数据返回null or 小于等于 0 ");
//			return;
//		}
//
//		//最新一次的同步订单打码金额
//		long orderAmount = new BigDecimal(orderSynVO.getCountAmount()).longValue();
//		// 剩余总实际打码 == 总实际打码 - 历史归档打码
//		long actualAmountCount = new BigDecimal(amountCount).subtract(new BigDecimal(orderSynVO.getCountAmount())).longValue();
//
//		// 总需求打码
//		long demandAmountCount = 0L;
//		for (BusinessVO vo : listBusVO) {
//			//充值需求打码 =  配置比例倍数 * 当前充值需求打码
//			demandAmountCount += toolsRate.multiply(new BigDecimal(vo.getActualAmount())).longValue();
//		}
//
//		// 总实际打码 < 总需求打码
//		if ( actualAmountCount < demandAmountCount ) {
//			log.info("总实际打码 < 总需求打码， 不处理消息");
//			return;
//		}
//
//		//当前数据同步批次
//		Long batchCode = idService.getNextId();
//		//当前批次时间
//		LocalDateTime date = LocalDateTime.now();
//
//		for (BusinessVO vo : listBusVO) {
//			// 打码值 == 当前充值需求打码 * 配置比率
//			long demand = toolsRate.multiply(new BigDecimal(vo.getActualAmount())).longValue();
//
//			//剩余总实际打码 == 剩余总实际打码 - 打码值
//			actualAmountCount = actualAmountCount - demand;
//
//			//修改当前充值记录状态
//			BusinessVO updateBusVO = new BusinessVO();
//			updateBusVO.setId(vo.getId());
//			updateBusVO.setPlatformId(platformId);
//			updateBusVO.setOrderCodeRate(toolsRate);
//			updateBusVO.setStatusCode(BusStatusEnums.CODE_SYN_OK.getVal());
//			businessService.update(updateBusVO);
//
//			//插入 剩余实际打码， 支付单号， 用户id， 平台id， 当前批次
//			OrderSyn entity = new OrderSyn();
//			entity.setBatchCode(batchCode);
//			entity.setId(batchCode);
//			entity.setUserId(userId);
//			entity.setPlatformId(platformId);
//			entity.setBusCode(String.valueOf(vo.getCode()));
//			entity.setBusId(vo.getId());
//			entity.setLastOrderDatetime(date);
//			entity.setLastAmount(String.valueOf(actualAmountCount));
//			entity.setCountAmount(String.valueOf(orderAmount + demand));
//			entity.setCreateTimestamp(LocalDateTime.now());
//
//			orderSynService.save(entity);
//		}
//
//		//插入数据到 打码订单归档表
//		List<OrderSynData> orderSynList = new ArrayList<>();
//		for (BetMoneyCodeParent.BetMoneyCodeMsg betMoneyCodeMsg : listOrder) {
//
//			OrderSynData insertEntity = new OrderSynData();
//
//			insertEntity.setPlatformId(platformId);
//			insertEntity.setUserId(userId);
//
//			insertEntity.setBatchCode(batchCode);
//			insertEntity.setGameId(betMoneyCodeMsg.getGameId());
//			insertEntity.setGameName(betMoneyCodeMsg.getGameName());
//			//金额  *  100 万
//			insertEntity.setMoney(new BigDecimal(betMoneyCodeMsg.getBetMoney()).multiply(new BigDecimal(BusConstant.UTILS_DIVISOR)).toString());
//			insertEntity.setOrderByName(betMoneyCodeMsg.getGameName());
//			insertEntity.setOrderByUserId(betMoneyCodeMsg.getUserId());
//			insertEntity.setOrderEndDatetime(betMoneyCodeMsg.getBetEndTime());
//			insertEntity.setOrderNo(betMoneyCodeMsg.getOrderNo());
//			insertEntity.setOrderRegDatetime(betMoneyCodeMsg.getBetStartTime());
//			insertEntity.setParamCalc(toolsRate.toString());
//			orderSynList.add(insertEntity);
//		}
//		orderSynDataService.saveBatch(orderSynList);
//
//	}
//
//	/**
//	 * RPC 查询打码配置
//	 * @param platformId
//	 * @return
//	 */
//	private BigDecimal getTools(Long platformId) {
//		//RPC查询当前用户的打码配置
//		InAndOutOfStyleQueReq req = new InAndOutOfStyleQueReq();
//		req.setPlatformId(platformId);
//		req.setEnumSetTheType(EnumSetTheType.WASH_CODE);
//		R<InAndOutOfStyleSettingResp> retSet = inAndOutOfStyleSettingFeignClient.getInAndOutOfStyleSettingInfo(req);
//		if (!retSet.isSuccess()) {
//			log.info("RPC未查询， 打码配置数据");
//			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
//		}
//
//		//配置的打码倍数
//		if(null == retSet.getData() || null == retSet.getData().getCodeSizeMultiple() ) {
//			log.info("RPC查询到 打码配置数据， 数据返回null or小于等于 0 ");
//			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
//		}
//
//		BigDecimal toolsRate = new BigDecimal(retSet.getData().getCodeSizeMultiple());
//		if( toolsRate.longValue() <= 0 ) {
//			log.info("RPC查询到 打码配置数据， 数据返回null or 小于等于 0 ");
//			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
//		}
//		//返回打码配置
//		return toolsRate;
//
//	}
//
//}
