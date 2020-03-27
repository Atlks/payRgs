package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformReleaseVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatformRelease;
import com.platform.top.xiaoyu.run.service.pay.mapper.PayPlatformReleaseMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IPayPlatformReleaseService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 第三方支付发布平台 服务实现类
 *
 * @author coffey
 */
@Service
public class PlatformReleaseServiceImpl extends AbstractMybatisPlusService<PayPlatformReleaseMapper, PayPlatformRelease, Long> implements IPayPlatformReleaseService {

	@Autowired
	private PayPlatformReleaseMapper releaseMapper;

	@Override
	public Page<PayPlatformReleaseVO> findPage(Page<PayPlatformReleaseVO> page, PayPlatformReleaseVO vo) {

		this.checkPlatformId(vo.getPlatformId());
		return releaseMapper.findPage(page, vo);
	}

	@Override
	public PayPlatformReleaseVO findDetail(Long id, Long platformId) {
		this.checkPlatformId(platformId);
		return this.findDetail(PayPlatformReleaseVO.builder().id(id).platformId(platformId).build());
	}

	@Override
	public PayPlatformReleaseVO findDetail(PayPlatformReleaseVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return releaseMapper.findDetail(vo);
	}

	@Override
	public List<PayPlatformReleaseVO> findListAll(PayPlatformReleaseVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return releaseMapper.findListAll(vo);
	}

	@Override
	public boolean insert(PayPlatformReleaseVO vo) {
		this.checkPlatformId(vo.getPlatformId());

		PayPlatformRelease entity = BeanCopyUtils.copyBean(vo, PayPlatformRelease.class);
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setPlatformId(entity.getPlatformId());

		return this.save(entity);

	}

	@Override
	public boolean update(PayPlatformReleaseVO vo) {
		this.checkPlatformId(vo.getPlatformId());

		PayPlatformRelease entity = BeanCopyUtils.copyBean(vo, PayPlatformRelease.class);
		entity.setUpdateTimestamp(LocalDateTime.now());

		QueryWrapper<PayPlatformRelease> queryWrapper = new QueryWrapper<PayPlatformRelease>();
		queryWrapper.eq(PayPlatformRelease.PK_ID, vo.getId()).eq(PayPlatformRelease.P_PLATFORM_ID, vo.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);
		QueryWrapper<PayPlatformRelease> queryWrapper = new QueryWrapper<PayPlatformRelease>();
		queryWrapper.in(PayPlatformRelease.PK_ID, ids).eq(PayPlatformRelease.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}
}
