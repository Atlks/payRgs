package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.WhitelistVO;
import com.platform.top.xiaoyu.run.service.pay.entity.Whitelist;
import com.platform.top.xiaoyu.run.service.pay.mapper.WhitelistMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IWhitelistService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 白名单 服务实现类
 *
 * @author coffey
 */
@Service
public class WhitelistServiceImpl extends AbstractMybatisPlusService<WhitelistMapper, Whitelist, Long> implements IWhitelistService {

	@Autowired
	private WhitelistMapper mapper;

	@Override
	public List<WhitelistVO> findListAll(WhitelistVO vo) {
		return mapper.findListAll(vo);
	}

	@Override
	public Page<WhitelistVO> findPage(Page<WhitelistVO> page, WhitelistVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findPage(page, vo);
	}

	@Override
	public WhitelistVO findDetail(Long id, Long platformId) {
		this.checkPlatformId(platformId);
		WhitelistVO vo = new WhitelistVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return this.findDetail(vo);
	}

	@Override
	public WhitelistVO findDetail(WhitelistVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(WhitelistVO req) {
		this.checkPlatformId(req.getPlatformId());
		Whitelist entity = BeanCopyUtils.copyBean(req, Whitelist.class);
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setPlatformId(req.getPlatformId());

		return this.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(WhitelistVO req) {
		this.checkPlatformId(req.getPlatformId());
		Whitelist entity = BeanCopyUtils.copyBean(req, Whitelist.class);
		entity.setUpdateTimestamp(LocalDateTime.now());

		QueryWrapper<Whitelist> queryWrapper = new QueryWrapper<Whitelist>();
		queryWrapper.eq(Whitelist.PK_ID, req.getId()).eq(Whitelist.P_PLATFORM_ID, req.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);

		QueryWrapper<Whitelist> queryWrapper = new QueryWrapper<Whitelist>();
		queryWrapper.in(Whitelist.PK_ID, ids).eq(Whitelist.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
