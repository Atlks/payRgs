package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TimeOutVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.UserLvlVO;

import java.util.List;

/**
 * 用户支付等级
 *
 * @author coffey
 */
public interface IUserLvlService {

	public List<UserLvlVO> findListAll(UserLvlVO req);

	public Page<UserLvlVO> findPage(Page<UserLvlVO> page, UserLvlVO vo);

	public UserLvlVO findDetail(Long id, Long platformId);

	public UserLvlVO findDetail(UserLvlVO vo);

	/** 查询用户等级明细  */
	public UserLvlVO findDetailUser(Long userId, Long platformId);

	/**  新增用户支付等级  */
	public boolean insert(UserLvlVO req);

	/**  修改用户支付等级  */
	public boolean update(UserLvlVO req);

	/**  批量删除用户支付等级  */
	public boolean delBatch(List<Long> ids, Long platformId);

	/** 计算用户支付等级 */
	public boolean calcPayLVL(Long userId, Long platformId, Long money, String sysKey);

	/**  分页查询用户下所有可用接口  */
	public Page<TimeOutVO> findUserPage(int page, int size, Long userId, Long platformId);

	/**  查询用户下所有可用接口  */
	public List<TimeOutVO> findUserListAll(Long userId, Long platformId);

}
