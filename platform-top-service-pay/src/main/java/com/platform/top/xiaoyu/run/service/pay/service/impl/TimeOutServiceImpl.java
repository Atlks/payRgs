package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TimeOutVO;
import com.platform.top.xiaoyu.run.service.pay.entity.TimeOut;
import com.platform.top.xiaoyu.run.service.pay.mapper.TimeOutMapper;
import com.platform.top.xiaoyu.run.service.pay.service.ITimeOutService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 接口超时配置 服务实现类
 *
 * @author coffey
 */
@Service
public class TimeOutServiceImpl extends AbstractMybatisPlusService<TimeOutMapper, TimeOut, Long> implements ITimeOutService {

	@Autowired
	private TimeOutMapper mapper;

	@Override
	public Page<TimeOutVO> findPage(Page<TimeOutVO> page, TimeOutVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findPage(page, vo);
	}

	@Override
	public Page<TimeOutVO> findPageLvl (Page<TimeOutVO> page, TimeOutVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findPageLvl(page, vo);
	}

	@Override
	public TimeOutVO findDetail(Long id, Long platformId) {

		this.checkPlatformId(platformId);

		TimeOutVO vo = new TimeOutVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return mapper.findDetail(vo);
	}

	@Override
	public TimeOutVO findDetail(TimeOutVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	public List<TimeOutVO> findListAll (TimeOutVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findListAll(vo);
	}

	@Override
	public List<TimeOutVO> findListAllLvl (TimeOutVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findListAllLvl(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(TimeOutVO req) {
		this.checkPlatformId(req.getPlatformId());
		TimeOutVO find_vo = new TimeOutVO();
		find_vo.setSysKey(req.getSysKey());
		find_vo.setPlatformId(req.getPlatformId());
		find_vo = findDetail(find_vo);

		if ( null != find_vo ) {
			throw new BizBusinessException(BasePayExceptionType.DATA_REPEAT);
		}

		TimeOut entity = BeanCopyUtils.copyBean(req, TimeOut.class);
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setPlatformId(req.getPlatformId());

		return this.save(entity);
	}

	@Override
	public boolean update(TimeOutVO req) {
		this.checkPlatformId(req.getPlatformId());
		TimeOutVO find_vo = new TimeOutVO();
		find_vo.setId(req.getId());
		find_vo.setPlatformId(req.getPlatformId());
		find_vo = findDetail(find_vo);

		if ( null == find_vo ) {
			throw new BizBusinessException(BasePayExceptionType.DATA_NULL);
		}

		TimeOut entity = BeanCopyUtils.copyBean(req, TimeOut.class);
		entity.setUpdateTimestamp(LocalDateTime.now());
		entity.setPlatformId(req.getPlatformId());

		QueryWrapper<TimeOut> queryWrapper = new QueryWrapper<TimeOut>();
		queryWrapper.eq(TimeOut.PK_ID, find_vo.getId()).eq(TimeOut.P_PLATFORM_ID, find_vo.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<TimeOut> queryWrapper = new QueryWrapper<TimeOut>();
		queryWrapper.in(TimeOut.PK_ID, ids).eq(TimeOut.P_PLATFORM_ID, platformId);

		return this.remove(queryWrapper);
	}

	@Override
	public int findTimeOut(String sysKey, Long platformId) {
		this.checkPlatformId(platformId);
		TimeOutVO vo = new TimeOutVO();
		vo.setSysKey(sysKey);
		vo.setPlatformId(platformId);
		vo = mapper.findDetail(vo);

		if( null == vo ) {
			return mapper.findSecond();
		}
		return vo.getTimeOutSecond();
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
