package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.vo.MsgBusVO;
import com.platform.top.xiaoyu.run.service.finance.entity.MsgBus;

import java.util.List;

/**
 * 消息金额变动
 *
 * @author coffey
 */
public interface IMsgBusService extends IService<MsgBus> {

	public Page<MsgBusVO> findPage(Page<MsgBusVO> page, MsgBusVO vo);

	public List<MsgBusVO> findListAll(MsgBusVO vo);

	public MsgBusVO findDetail(MsgBusVO vo);

	/**
	 * 查询是否存在 当前消息
	 * @param code 消息编码
	 * @param platformId 平台ID
	 * @param enums 交易类型 枚举
	 * @return true 不存在， false 存在
	 */
	public boolean findMsg(String code, Long platformId, BusTypeEnums enums);

	public MsgBusVO findDetailUserId(Long userId, Long platformId);

	public MsgBusVO findDetailId(Long id, Long platformId);

	public boolean insert(MsgBusVO vo);

	public boolean update(MsgBusVO vo);

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
	public void insertMQ(Long userId, Long platformId, String code, String amountDB, BusTypeEnums typeEnums, CommonStatus status, String msg);

}
