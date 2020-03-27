package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TokenPayVO;
import com.platform.top.xiaoyu.run.service.pay.entity.TokenPay;
import com.platform.top.xiaoyu.run.service.pay.mapper.TokenPayMapper;
import com.platform.top.xiaoyu.run.service.pay.service.ITokenPayService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.digest.md5.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 安全认证token 服务实现类
 *
 * @author coffey
 */
@Service
public class TokenPayServiceImpl extends AbstractMybatisPlusService<TokenPayMapper, TokenPay, Long> implements ITokenPayService {

	@Autowired
	private TokenPayMapper mapper;

	@Override
	public Page<TokenPayVO> findPage(Page<TokenPayVO> page, TokenPayVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findPage(page, vo);
	}

	@Override
	public TokenPayVO findDetail(Long id, Long platformId) {
		TokenPayVO vo = new TokenPayVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return this.findDetail(vo);
	}

	@Override
	public TokenPayVO findDetail(TokenPayVO vo) {
		this.checkPlatformId(vo.getPlatformId());
		return mapper.findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(TokenPayVO req) {
		this.checkPlatformId(req.getPlatformId());
		TokenPayVO find_vo = new TokenPayVO();
		find_vo.setSysKey(req.getSysKey());
		find_vo.setPlatformId(req.getPlatformId());
		find_vo = mapper.findDetail(find_vo);
		if (null != find_vo) {
			throw new BizBusinessException(BasePayExceptionType.DATA_REPEAT);
		}

		TokenPay entity = BeanCopyUtils.copyBean(req, TokenPay.class);
		entity.setCreateTimestamp(LocalDateTime.now());
		entity.setToken(MD5Util.string2MD5HexUpper(LocalDateTime.now().toString()));
		entity.setPlatformId(req.getPlatformId());

		return this.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(TokenPayVO req) {
		this.checkPlatformId(req.getPlatformId());
		TokenPayVO find_vo = new TokenPayVO();
		find_vo.setSysKey(req.getSysKey());
		find_vo.setPlatformId(req.getPlatformId());
		find_vo = mapper.findDetail(find_vo);
		if (null != find_vo) {
			throw new BizBusinessException(BasePayExceptionType.DATA_REPEAT_UPDATE);
		}

		TokenPay entity = BeanCopyUtils.copyBean(req, TokenPay.class);
		entity.setUpdateTimestamp(LocalDateTime.now());
		entity.setSysKey(req.getSysKey());
		entity.setSysName(req.getSysName());

		QueryWrapper<TokenPay> queryWrapper = new QueryWrapper<TokenPay>();
		queryWrapper.eq(TokenPay.PK_ID, req.getId()).eq(TokenPay.P_PLATFORM_ID, req.getPlatformId());

		return this.update(entity, queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delBatch(List<Long> ids, Long platformId) {
		this.checkPlatformId(platformId);

		QueryWrapper<TokenPay> queryWrapper = new QueryWrapper<TokenPay>();
		queryWrapper.in(TokenPay.PK_ID, ids).eq(TokenPay.P_PLATFORM_ID, platformId);

		return this.remove(queryWrapper);
	}

	@Override
	public boolean findToken(Long platformId, String sysKey, String token) {
		TokenPayVO vo = new TokenPayVO();
		vo.setSysKey(sysKey);
		vo.setToken(token);
		vo.setPlatformId(platformId);
		vo = mapper.findDetail(vo);
		if(null != vo) {
			return true;
		}
		return false;
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
