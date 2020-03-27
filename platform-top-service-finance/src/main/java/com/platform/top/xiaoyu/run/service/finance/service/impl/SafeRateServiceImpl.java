package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.exception.SafeExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeRateVO;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeRate;
import com.platform.top.xiaoyu.run.service.finance.mapper.SafeRateMapper;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeRateService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 保险箱利率 服务实现类
 *
 * @author xiaoyu
 */
@Service
public class SafeRateServiceImpl extends AbstractMybatisPlusService<SafeRateMapper, SafeRate, Long> implements ISafeRateService {

	@Autowired
	private SafeRateMapper mapper;

	@Override
	public Page<SafeRateVO> findPage(Page<SafeRateVO> page, SafeRateVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findPage(page, vo);
	}

	@Override
	public List<SafeRateVO> findListAll(SafeRateVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findListAll(vo);
	}

	@Override
	public SafeRateVO findDetail(SafeRateVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(SafeRateVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询当前用户保险箱设置是否存在
		SafeRateVO find_vo = new SafeRateVO();
		find_vo.setDayNum(vo.getDayNum());
		find_vo.setPlatformId(vo.getPlatformId());
		find_vo = this.findDetail(find_vo);

		if( null != find_vo ) {
			throw new BizBusinessException(SafeExceptionType.DATA_NOT_NULL);
		}
		vo.setCreateTimestamp(LocalDateTime.now());
		return this.save(BeanCopyUtils.copyBean(vo, SafeRate.class));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(SafeRateVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		UpdateWrapper<SafeRate> updateWrapper = new UpdateWrapper<SafeRate>();
		updateWrapper.eq(SafeRate.PK_ID, vo.getId()).eq(SafeRate.P_PLATFORM_ID, vo.getPlatformId());

		vo.setUpdateTimestamp(LocalDateTime.now());
		return this.update(BeanCopyUtils.copyBean(vo, SafeRate.class), updateWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean del(List<Long> ids, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<SafeRate> queryWrapper = new QueryWrapper<SafeRate>();
		queryWrapper.in(SafeRate.PK_ID, ids).eq(SafeRate.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}


}
