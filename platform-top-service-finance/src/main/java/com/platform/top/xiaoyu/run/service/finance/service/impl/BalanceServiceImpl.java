package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTradingMannerEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.finance.service.IBalanceService;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.platform.top.xiaoyu.run.service.finance.service.IFlowService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.chwin.firefighting.apiserver.data.MybatisUtil4game;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * 新增我的账本余额 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class BalanceServiceImpl implements IBalanceService {

	@Autowired
	private IBookService iBookService;
	@Autowired
	private IFlowService flowService;

	/**
	 *
	 * @param userId 用户Id
	 * @param platformId  平台Id
	 * @param userName 用户名
	 * @param amount 金额
	 * @param type 类型 1 登入、2 登出、3 充值、4 提现、5 人工取款、6 会员取款、7 领取佣金、8 红包、9 保险箱记录、10 结算入账、11 领取洗码、12 赠送彩金、13 活动优惠、14 人工存入、15 会员充值
	 * @param typeAll 大类
	 * @param remarks 备注
	 */
	@Override
	public void execute(Long userId, Long platformId, String userName, String amount, BusTypeAllEnums typeAll, BusTypeEnums type, String remarks) {

		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		//查询当前用户是否存在账本， 不存在则新增账本
		BookVO bookVO = iBookService.inserBook(userId, platformId, userName);

		Integer status = CommonStatus.ENABLE.getValue();
		//当前用户存在账本， 新增可用余额
		if ( !iBookService.addBalanceRecharge(bookVO.getId(), platformId, amount) ) {
			//账本累加出错
			status = CommonStatus.DISABLE.getValue();
		}

		if ( status.intValue() == CommonStatus.DISABLE.getValue().intValue() ) {
			log.info("账本，新增可用余额错误。");
			return;
		}
		//插入交易流水
		this.inserFlow(userId, platformId, amount, typeAll, type, remarks);

	}
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	/**
	 * 插入交易流水
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param amount 金额
	 * @param typeAll 大类
	 * @param type 小类
	 * @param remarks 备注
	 */
	private void inserFlow(Long userId, Long platformId, String amount, BusTypeAllEnums typeAll, BusTypeEnums type, String remarks ) {

		LocalDateTime date = LocalDateTime.now();
		//插入交易流水
		FlowVO flowVO = new FlowVO();

		flowVO.setAmount(amount);
		flowVO.setActualAmount(amount);
		flowVO.setUserId(userId);
		flowVO.setPlatformId(platformId);
		flowVO.setBusTimestamp(date);
		flowVO.setUpdateTimestamp(date);
		flowVO.setCreateTimestamp(date);

		//活动-可用余额累加
		flowVO.setTypeAll(typeAll.getVal());
		//提现通过
		flowVO.setType(type.getVal());
		flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());

		//人工出账通过，冻结金额扣减且核销
		flowVO.setRemark(remarks);
		flowVO.setDescription(remarks);
		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FAIL);
		}
	}

//统计人数，金额 ，分组聚合运算，分组依据 日期，平台， sumPplSumBalance grouby Date,plat
	public  Object sumPplSumBalanceDateGrupbyPlat()
	{
		List li = sqlSessionTemplate.selectList("balanceHistoryListCurrentGroupbyPlatform_id", null);

		li.forEach(new Consumer() {
			@Override
			public void accept(Object rec) {
                 try{
					 sqlSessionTemplate.update("balanceHistoryListInsert", rec);
				 }catch(Exception e)
				 {
					 System.out.println(e.getMessage());
				 }

			}
		});
		return li;
	}

}
