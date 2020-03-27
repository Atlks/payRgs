package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TokenPayVO;

import java.util.List;

/**
 * 安全认证token
 *
 * @author coffey
 */
public interface ITokenPayService {

	public Page<TokenPayVO> findPage(Page<TokenPayVO> page, TokenPayVO vo);

	public TokenPayVO findDetail(Long id, Long platformId);

	public TokenPayVO findDetail(TokenPayVO vo);

	public boolean insert(TokenPayVO req);

	public boolean update(TokenPayVO req);

	public boolean delBatch(List<Long> ids, Long platformId);

	/** 查询当前调用方系统的token 鉴权 */
	public boolean findToken(Long platformId, String sysKey, String token);

}
