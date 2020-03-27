package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayFlowVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayFlow;
import com.platform.top.xiaoyu.run.service.pay.mapper.PayFlowMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IPayFlowService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付流水 服务实现类
 *
 * @author coffey
 */
@Service
public class PayFlowServiceImpl extends AbstractMybatisPlusService<PayFlowMapper, PayFlow, Long> implements IPayFlowService {

	@Autowired
	private PayFlowMapper mapper;

	@Override
	public Page<PayFlowVO> findPage(Page<PayFlowVO> page, PayFlowVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findPage(page, vo);
	}

	@Override
	public List<PayFlowVO> findListAll(PayFlowVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findListAll(vo);
	}

	@Override
	public PayFlowVO findDetail(Long id, Long platformId) {
		PayFlowVO vo = new PayFlowVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return this.findDetail(vo);
	}

	@Override
	public PayFlowVO findDetail(PayFlowVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	public boolean insert(PayFlowVO req) {

		this.checkPlatformId(req.getPlatformId());

		PayFlow entity = BeanCopyUtils.copyBean(req, PayFlow.class);
		entity.setCreateTimestamp(LocalDateTime.now());

		return this.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(PayFlowVO req) {
		this.checkPlatformId(req.getPlatformId());
		PayFlow entity = BeanCopyUtils.copyBean(req, PayFlow.class);
		entity.setUpdateTimestamp(LocalDateTime.now());

		QueryWrapper<PayFlow> queryWrapper = new QueryWrapper<PayFlow>();
		queryWrapper.eq(PayFlow.PK_ID, req.getId()).eq(PayFlow.P_PLATFORM_ID, req.getPlatformId());

		return this.update(entity, queryWrapper);
	}


	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
