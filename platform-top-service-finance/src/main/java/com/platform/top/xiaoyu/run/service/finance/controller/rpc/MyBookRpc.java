package com.platform.top.xiaoyu.run.service.finance.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.feign.IBookFeignClient;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.BaseIdUserIdPlatIdReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.book.BookSumReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.SignInReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.SignOutReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.BookResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookSumResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.SignInResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.SignOutResp;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的账本 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.RPC_BOOK)
@Api(value = "我的账本", tags = "我的账本")
@RpcController
public class MyBookRpc extends TopBaseController implements IBookFeignClient {


	@Autowired
	private IBookService bookService;

	/**
	 * 查询我的账本明细： 可用余额
	 * @return
	 */
	@PostMapping("/findDetail")
	@ApiOperation(value = "查询我的账本明细", notes = "查询我的账本明细")
	@ApiLog("查询我的账本明细")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<BookResp> findDetail(@RequestBody @Validated BaseIdUserIdPlatIdReq req) {
		if( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		BookVO bookVO = bookService.findDetail(req.getUserId(), req.getPlatformId());
		if(null != bookVO) {
			BookResp resp = BeanCopyUtils.copyBean(bookVO, BookResp.class);
			resp.setBalanceStr(bookVO.getBalance());
			resp.setBalanceSafeStr(bookVO.getBalanceSafe());
			return R.data(resp);
		}
		return R.success("未查询到数据");
	}

	@PostMapping("/signIn")
	@ApiOperation(value = "登入游戏", notes = "登入游戏")
	@ApiLog("登入游戏")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<SignInResp> signIn(@RequestBody SignInReq req) {
		if( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		if( null == req.getPlatformId() || req.getPlatformId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		//登入游戏， 可用余额清零， 冻结金额累加, 新增一条冻结信息。 返回用户可用余额
		String bal =  bookService.signIn(req.getUserId(), req.getPlatformId(),
				req.getUserName(), req.getGameName(), req.getGameType());

		Map<String, String> map = new HashMap<String, String>();
		//解析字符串后尾数.000舍弃
		map.put("BalanceStr", bal.split("\\.")[0]);

		return R.data(SignInResp.builder().balanceStr(bal).sign(bookService.sign(map)).build());
	}

	@PostMapping("/signOut")
	@ApiOperation(value = "登出游戏", notes = "登出游戏")
	@ApiLog("登出游戏")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<SignOutResp> signOut(@RequestBody @Validated SignOutReq req) {
		if( null == req.getUserId() || req.getUserId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		if( null == req.getPlatformId() || req.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		if(StringUtils.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}
		SignOutResp build = SignOutResp.builder().balanceStr(bookService.signOut(req.getUserId(), req.getPlatformId(),
			req.getAmount(), req.getGameName(), req.getGameType())).build();

		Map<String, String> map = new HashMap<String, String>();
		map.put("BalanceStr",build.getBalanceStr().split("\\.")[0]);

		build.setSign(bookService.sign(map));
		return R.data(build);
	}

	@PostMapping("/findBalanceSum")
	@ApiOperation(value = "查询计总余额数据", notes = "查询计总余额数据")
	@ApiLog("查询计总余额数据")
	@Override
	public R<BookSumResp> findBalanceSum(@RequestBody BookSumReq req) {

		return R.data(null);
	}


}
