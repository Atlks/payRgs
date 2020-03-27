package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayInfoVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayInfo;
import com.platform.top.xiaoyu.run.service.pay.mapper.PayInfoMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IPayInfoService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 芒果支付系统对接 服务实现类
 *
 * @author coffey
 */
@Service
public class PayInfoServiceImpl extends AbstractMybatisPlusService<PayInfoMapper, PayInfo, Long> implements IPayInfoService {

	@Autowired
	private PayInfoMapper mapper;

	@Override
	public Page<PayInfoVO> findPage(Page<PayInfoVO> page, PayInfoVO vo) {
		if(null == vo || null == vo.getPlatformId() || vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findPage(page, vo);
	}

	@Override
	public List<PayInfoVO> findListAll(PayInfoVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findListAll(vo);
	}

	@Override
	public PayInfoVO findDetailId(Long id, Long platformId) {
		PayInfoVO vo = new PayInfoVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return this.findDetail(vo);
	}

	@Override
	public PayInfoVO findDetail(PayInfoVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(PayInfoVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		PayInfo entity = BeanCopyUtils.copyBean(vo, PayInfo.class);
		entity.setUpdateTimestamp(LocalDateTime.now());

		QueryWrapper<PayInfo> queryWrapper = new QueryWrapper<PayInfo>();
		queryWrapper.eq(PayInfo.PK_ID, vo.getId()).eq(PayInfo.P_PLATFORM_ID, vo.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	public boolean insert(PayInfoVO vo) {
		this.checkPlatformId(vo.getPlatformId());

		return this.save(BeanCopyUtils.copyBean(vo, PayInfo.class));
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
