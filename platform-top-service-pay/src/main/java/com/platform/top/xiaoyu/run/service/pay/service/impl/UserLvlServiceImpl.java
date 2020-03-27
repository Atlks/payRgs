package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TimeOutVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.ToolsPayVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.UserLvlVO;
import com.platform.top.xiaoyu.run.service.pay.entity.UserLvl;
import com.platform.top.xiaoyu.run.service.pay.mapper.UserLvlMapper;
import com.platform.top.xiaoyu.run.service.pay.service.ITimeOutService;
import com.platform.top.xiaoyu.run.service.pay.service.IToolsPayService;
import com.platform.top.xiaoyu.run.service.pay.service.IUserLvlService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户支付等级 服务实现类
 *
 * @author coffey
 */
@Service
public class UserLvlServiceImpl extends AbstractMybatisPlusService<UserLvlMapper, UserLvl, Long> implements IUserLvlService {

	@Autowired
	private UserLvlMapper mapper;
	@Autowired
	private ITimeOutService iTimeOutService;
	@Autowired
	private IToolsPayService toolsPayService;

	@Override
	public List<UserLvlVO> findListAll(UserLvlVO req) {
		this.checkPlatformId(req.getPlatformId());
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		return mapper.findListAll(vo);
	}

	@Override
	public Page<UserLvlVO> findPage(Page<UserLvlVO> page, UserLvlVO req) {
		this.checkPlatformId(req.getPlatformId());
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		return mapper.findPage(page, vo);
	}

	@Override
	public UserLvlVO findDetail(Long id, Long platformId) {
		this.checkPlatformId(platformId);
		UserLvlVO userLvlVO = new UserLvlVO();
		userLvlVO.setId(id);
		userLvlVO.setPlatformId(platformId);
		return mapper.findDetail(userLvlVO);
	}

	@Override
	public UserLvlVO findDetail(UserLvlVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	public UserLvlVO findDetailUser(Long userId, Long platformId) {
		this.checkPlatformId(platformId);
		UserLvlVO userLvlVO = new UserLvlVO();
		userLvlVO.setUserId(userId);
		userLvlVO.setPlatformId(platformId);
		return this.findDetail(userLvlVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(UserLvlVO param) {
		this.checkPlatformId(param.getPlatformId());
		//接口key 为空
		if( StringUtils.isEmpty(param.getSysKey()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL_INTERFACEKEY);
		}

		//查询等级积分配置
		ToolsPayVO toolsPayVO_find = new ToolsPayVO();
		toolsPayVO_find.setLvl(param.getLvl());
		toolsPayVO_find.setSysKey(param.getSysKey());
		toolsPayVO_find.setPlatformId(param.getPlatformId());
		toolsPayVO_find  = toolsPayService.findDetail(toolsPayVO_find);
		if(null == toolsPayVO_find) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}

		//插入数据
		UserLvl entity = new UserLvl();
		entity.setNumber(toolsPayVO_find.getNumber());
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setPlatformId(param.getPlatformId());
		entity.setUserId(param.getUserId());
		entity.setLvl(1);
		entity.setSysKey(param.getSysKey());

		return this.save(entity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(UserLvlVO param) {
		this.checkPlatformId(param.getPlatformId());
		if( StringUtils.isEmpty(param.getSysKey()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL_INTERFACEKEY);
		}

		//拉取接口积分等级配置， 积分清理到当前等级的积分值
		ToolsPayVO toolsPayVO = new ToolsPayVO();
		toolsPayVO.setSysKey(param.getSysKey());
		toolsPayVO.setLvl(param.getLvl());
		toolsPayVO.setPlatformId(param.getPlatformId());
		toolsPayVO = toolsPayService.findDetail(toolsPayVO);
		if(null == toolsPayVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}

		//更新当前用户的支付接口等级积分
		UserLvl entity = BeanCopyUtils.copyBean(param, UserLvl.class);
		entity.setUpdateTimestamp(LocalDateTime.now());
		entity.setNumber(toolsPayVO.getNumber());
		entity.setLvl(toolsPayVO.getLvl());

		QueryWrapper<UserLvl> queryWrapper = new QueryWrapper<UserLvl>();
		queryWrapper.eq(UserLvl.PK_ID, param.getId()).eq(UserLvl.P_PLATFORM_ID, param.getPlatformId());

		return this.update(entity, queryWrapper);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);

		QueryWrapper<UserLvl> queryWrapper = new QueryWrapper<UserLvl>();
		queryWrapper.in(UserLvl.PK_ID, ids).eq(UserLvl.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean calcPayLVL(Long userId, Long platformId, Long money, String sysKey) {
		this.checkPlatformId(platformId);
		//等级、分值
		int tools_lvl = 1, tools_number = 0;

		//1. 查询当前用户数据行是否存在，不存在则插入一条新数据
		UserLvlVO userLvlVO = this.findDetailUser(userId, platformId);
		if( null == userLvlVO ) {
			userLvlVO = new UserLvlVO();

			userLvlVO.setUserId(userId);
			userLvlVO.setNumber(tools_number);
			userLvlVO.setCreateTimestamp(LocalDateTime.now());
			userLvlVO.setSysKey(sysKey);

			if( !StringUtils.isEmpty(sysKey) ){
				userLvlVO.setLvl(tools_lvl);
			}

			this.save(BeanCopyUtils.copyBean(userLvlVO, UserLvl.class));
		} else {

			//2. 查询支付积分等级配置，按实际支付金额，可累计积分计算等级, 1元 == 100 积分
			ToolsPayVO toolsPayVO = new ToolsPayVO();

			if (!StringUtils.isEmpty(sysKey)) {
				//查询当前用户的级别
				toolsPayVO.setSysKey(sysKey);
				toolsPayVO.setLvl(userLvlVO.getLvl());
				toolsPayVO = toolsPayService.findDetail(toolsPayVO);
			} else {
				//未查询到用户数据， 查系统默认通用数据
				toolsPayVO = toolsPayService.findPriority(platformId);
			}

			//用户 等级、分值
			int lvl = 1, number = 0;
			//设置不为空
			if (null != toolsPayVO) {
				//设置的值
				number = toolsPayVO.getNumber();
				lvl = toolsPayVO.getLvl();
			}

			//3. 根据用户充值金额计算等级

			//当前用户分值
			number += money*100;

			//分值大于当前等级， 级别+1
			if(number >= tools_number) {
				lvl += 1;
			}

			//更新用户支付接口等级
			UserLvl entity = new UserLvl();
			entity.setLvl(lvl);
			entity.setNumber(number);

			entity.setId(userLvlVO.getUserId());

			int count = mapper.updateById(entity);

			if (count > 0 )
				return true;
		}
		return false;
	}

	@Override
	public Page<TimeOutVO> findUserPage(int page, int size, Long userId, Long platformId) {
		this.checkPlatformId(platformId);
		//1. 查询我的等级
		UserLvlVO find_vo = this.findDetail(userId, platformId);
		int lvl = 1;
		if(null != find_vo) {
			lvl = find_vo.getLvl();
		}
		Page<TimeOutVO> pagevo = new Page<TimeOutVO>(page, size);
		TimeOutVO timeOutVO = new TimeOutVO();
		timeOutVO.setLvl(lvl);

		//2. 根据等级参数， 分页查询所有支付接口
		return iTimeOutService.findPageLvl(pagevo, timeOutVO);
	}

	@Override
	public List<TimeOutVO> findUserListAll(Long userId, Long platformId) {
		this.checkPlatformId(platformId);
		//1. 查询我的等级
		UserLvlVO vo = this.findDetail(userId, platformId);

		int lvl = 1;
		if(null != vo) {
			lvl = vo.getLvl();
		}
		//2. 根据等级参数， 查询所有支付接口
		TimeOutVO timeOutVO = new TimeOutVO();
		timeOutVO.setLvl(lvl);

		return iTimeOutService.findListAllLvl(timeOutVO);
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}
}
