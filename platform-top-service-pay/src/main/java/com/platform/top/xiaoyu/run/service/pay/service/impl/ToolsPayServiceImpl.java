package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.ToolsPayVO;
import com.platform.top.xiaoyu.run.service.pay.entity.ToolsPay;
import com.platform.top.xiaoyu.run.service.pay.mapper.ToolsPayMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IToolsPayService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户支付接口等级积分配置 服务实现类
 *
 * @author coffey
 */
@Service
public class ToolsPayServiceImpl extends AbstractMybatisPlusService<ToolsPayMapper, ToolsPay, Long> implements IToolsPayService {

	@Autowired
	private ToolsPayMapper mapper;

	@Override
	public List<ToolsPayVO> findListAll(ToolsPayVO req) {
		this.checkPlatformId(req.getPlatformId());
		ToolsPayVO vo = BeanCopyUtils.copyBean(req, ToolsPayVO.class);
		return mapper.findListAll(vo);
	}

	@Override
	public Page<ToolsPayVO> findPage(Page<ToolsPayVO> page, ToolsPayVO req) {
		this.checkPlatformId(req.getPlatformId());
		ToolsPayVO vo = BeanCopyUtils.copyBean(req, ToolsPayVO.class);
		return mapper.findPage(page, vo);
	}

	@Override
	public ToolsPayVO findDetail(Long id, Long platformId) {
		this.checkPlatformId(platformId);
		ToolsPayVO vo = new ToolsPayVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return mapper.findDetail(vo);
	}

	@Override
	public ToolsPayVO findDetail(ToolsPayVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	public ToolsPayVO findPriority(Long platformId) {
		this.checkPlatformId(platformId);
		return mapper.findPriority(platformId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(ToolsPayVO req) {
		this.checkPlatformId(req.getPlatformId());
		//查询 数据是否存在
		ToolsPayVO find_pri = null;
		if (!StringUtils.isEmpty(req.getSysKey())) {
			//查询接口数据
			find_pri = new ToolsPayVO();
			find_pri.setLvl(req.getLvl());
			find_pri.setSysKey(req.getSysKey());
			find_pri.setPlatformId(req.getPlatformId());
			find_pri = mapper.findDetail(find_pri);
		} else {
			//查询通用数据
			find_pri = mapper.findPriority(req.getPlatformId());
		}

		if( null != find_pri ) {
			throw new BizBusinessException(BasePayExceptionType.DATA_REPEAT);
		}

		//不存在执行插入数据
		ToolsPay entity = BeanCopyUtils.copyBean(req, ToolsPay.class);
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setPlatformId(req.getPlatformId());

		if (!StringUtils.isEmpty(req.getSysKey())) {
			//接口key 不为空， 设置为 1
			entity.setPriority(1);
		}

		int count = mapper.insert(entity);
		if( count > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(ToolsPayVO vo) {

		this.checkPlatformId(vo.getPlatformId());

		ToolsPay entity = BeanCopyUtils.copyBean(vo, ToolsPay.class);
		entity.setUpdateTimestamp(LocalDateTime.now());
		entity.setPlatformId(vo.getPlatformId());

		QueryWrapper<ToolsPay> queryWrapper = new QueryWrapper<ToolsPay>();
		queryWrapper.eq(ToolsPay.PK_ID, vo.getId()).eq(ToolsPay.P_PLATFORM_ID, vo.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<ToolsPay> queryWrapper = new QueryWrapper<ToolsPay>();
		queryWrapper.in(ToolsPay.PK_ID, ids).eq(ToolsPay.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}
}
