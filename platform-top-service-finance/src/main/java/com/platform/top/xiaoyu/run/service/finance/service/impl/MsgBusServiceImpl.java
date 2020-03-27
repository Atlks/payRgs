package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.MsgBusVO;
import com.platform.top.xiaoyu.run.service.finance.entity.MsgBus;
import com.platform.top.xiaoyu.run.service.finance.mapper.MsgBusMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IMsgBusService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 消息金额变动 服务实现类
 *
 * @author xiaoyu
 */
@Service
@Slf4j
public class MsgBusServiceImpl extends AbstractMybatisPlusService<MsgBusMapper, MsgBus, Long> implements IMsgBusService {

	@Autowired
	private MsgBusMapper mapper;

	@Override
	public Page<MsgBusVO> findPage(Page<MsgBusVO> page, MsgBusVO vo) {
		return mapper.findPage(page, vo);
	}

	@Override
	public MsgBusVO findDetailId(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		MsgBusVO vo = new MsgBusVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return findDetail(vo);
	}

	@Override
	public MsgBusVO findDetail(MsgBusVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(vo);
	}

	@Override
	public boolean findMsg(String code, Long platformId, BusTypeEnums enums) {
		MsgBusVO vo = new MsgBusVO();
		vo.setCode(code);
		vo.setStatuss(CommonStatus.ENABLE.getValue());
		vo.setType(enums.getVal());
		vo.setPlatformId(platformId);
		if ( null == this.findDetail(vo) ) {
			return true;
		}
		return false;
	}

	@Override
	public List<MsgBusVO> findListAll(MsgBusVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findListAll(vo);
	}

	@Override
	public MsgBusVO findDetailUserId(Long userId, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		MsgBusVO vo = new MsgBusVO();
		vo.setUserId(userId);
		vo.setPlatformId(platformId);
		return findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(MsgBusVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return this.save(BeanCopyUtils.copyBean(vo, MsgBus.class));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(MsgBusVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		UpdateWrapper<MsgBus> updateWrapper = new UpdateWrapper<MsgBus>();
		updateWrapper.eq(MsgBus.PK_ID, vo.getId()).eq(MsgBus.P_PLATFORM_ID, vo.getPlatformId());
		return this.update(BeanCopyUtils.copyBean(vo, MsgBus.class), updateWrapper);
	}

	/**
	 * 插入MQ消息
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param code 消息CODE
	 * @param amountDB 金额
	 * @param typeEnums 消息类型
	 * @param status 状态
	 * @param msg 所有消息内容
	 */
	@Override
	public void insertMQ(Long userId, Long platformId, String code, String amountDB, BusTypeEnums typeEnums, CommonStatus status, String msg) {

		MsgBus msgBus = new MsgBus();
		msgBus.setStatuss(status.getValue());

		//查询当前消息是否已存在，已消费
		MsgBusVO findMsgVO = this.findDetail(MsgBusVO.builder()
				.code(code)
				.type(typeEnums.getVal())
				.platformId(platformId).build());

		if( null == findMsgVO) {
			msgBus.setUserId(userId);
			msgBus.setPlatformId(platformId);
			msgBus.setCode(code);
			msgBus.setAmount(amountDB);
			msgBus.setType(typeEnums.getVal());
			msgBus.setTypeStr(typeEnums.getName());
			msgBus.setMsg(msg);

			this.save(msgBus);

		} else {
			if ( findMsgVO.getStatuss().intValue() == CommonStatus.ENABLE.getValue() )  {
				//已执行
				log.info("登出游戏 消息已消费。" + code);
				return;
			}
			if( CommonStatus.ENABLE == status ) {
				msgBus.setUpdateTimestamp(LocalDateTime.now());

				UpdateWrapper<MsgBus> updateWrapper = new UpdateWrapper<MsgBus>();
				updateWrapper.eq(MsgBus.P_CODE, code).eq(MsgBus.P_PLATFORM_ID, platformId);
				this.update(msgBus, updateWrapper);
			}
		}
	}

}
