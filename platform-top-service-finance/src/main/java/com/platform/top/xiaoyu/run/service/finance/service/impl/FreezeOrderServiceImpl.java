package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FreezeOrderVO;
import com.platform.top.xiaoyu.run.service.finance.entity.FreezeOrder;
import com.platform.top.xiaoyu.run.service.finance.mapper.FreezeOrderMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IFreezeOrderService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 冻结金额 服务实现类
 *
 * @author xiaoyu
 */
@Service
public class FreezeOrderServiceImpl extends AbstractMybatisPlusService<FreezeOrderMapper, FreezeOrder, Long> implements IFreezeOrderService {

	@Autowired
	private FreezeOrderMapper mapper;

	@Override
	public Page<FreezeOrderVO> findPage(Page<FreezeOrderVO> page, FreezeOrderVO vo) {
		return mapper.findPage(page, vo);
	}

	@Override
	public FreezeOrderVO findDetailId(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		FreezeOrderVO vo = new FreezeOrderVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return mapper.findDetail(vo);
	}

	@Override
	public FreezeOrderVO findDetail(FreezeOrderVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(vo);
	}

	@Override
	public List<FreezeOrderVO> findListAll(FreezeOrderVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findListAll(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(FreezeOrderVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setCreateTimestamp(LocalDateTime.now());
		FreezeOrder entity = BeanCopyUtils.copyBean(vo, FreezeOrder.class);
		return this.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(FreezeOrderVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		UpdateWrapper<FreezeOrder> updateWrapper = new UpdateWrapper<FreezeOrder>();
		updateWrapper.eq(FreezeOrder.PK_ID, vo.getId()).eq(FreezeOrder.P_PLATFORM_ID, vo.getPlatformId());

		vo.setUpdateTimestamp(LocalDateTime.now());
		return this.update(BeanCopyUtils.copyBean(vo, FreezeOrder.class), updateWrapper);
	}
}
