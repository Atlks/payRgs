package com.platform.top.xiaoyu.run.service.finance.controller.frontdesk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.AccessToken;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.BaseUserIdReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.BookApiReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.BaseStatusResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.BookResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookCountResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookFlowApiResp;
import com.platform.top.xiaoyu.run.service.api.threadgame.feign.IThreadGameFeignClient;
import com.platform.top.xiaoyu.run.service.api.threadgame.vo.req.RefreshBalanceReq;
import com.platform.top.xiaoyu.run.service.finance.enums.BusTypeApiEnums;
import com.platform.top.xiaoyu.run.service.finance.service.IBookApiService;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.swagger.controller.FrontdeskController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的账本 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.FRONTDESK_BOOK)
@Api(value = "我的账本", tags = "我的账本")
@FrontdeskController
@Slf4j
public class MyBookApi extends TopBaseController {

	@Autowired
	private IBookService bookService;
	@Autowired
	private IBookApiService apiService;
	@Autowired
	private IThreadGameFeignClient iThreadGameFeignClient;

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询我的账本可用余额", notes = "查询我的账本可用余额")
	@ApiLog("查询我的账本可用余额")
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<BookResp> findDetail(@RequestBody BaseUserIdReq req) {
		if( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		AccessToken accessToken = SecurityUtil.getLoginAccount();
		if( req.getUserId().longValue() != accessToken.getUserId().longValue() ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}

		try {
			//调用 rpc 强制下分
			RefreshBalanceReq client = new RefreshBalanceReq();
			client.setUserId(req.getUserId());
			R<Boolean> retClient = iThreadGameFeignClient.refreshBalance(client);
			if ( retClient.isSuccess() ) {
				if ( null != retClient.getData() && !retClient.getData()) {
					log.info("调用远程下分异常。 返回数据 ==》" + retClient.getData());
				}
			} else {
				log.info("未能调通远程下分接口。 isSuccess 返回==》"+ retClient.isSuccess()+", msg ==》" + retClient.getMsg());
			}
		} catch (Exception e) {
			log.error("rpc远程接口异常，往下执行查询可用余额，捕获异常不处理当前异常。 " + e.getMessage(), e);
		}

		//查询可用余额
		BookVO bookVO = bookService.findDetail(accessToken.getUserId(), accessToken.getPlatformId());
		if(null == bookVO) {
			//查询无账号，则新增
			bookVO = bookService.inserBook(accessToken.getUserId(), accessToken.getPlatformId(), accessToken.getUserName());
		}
		BookResp resp = BeanCopyUtils.copyBean(bookVO, BookResp.class);
		if(null != resp) {
			resp.setBalanceStr(bookVO.getBalance());
			resp.setBalanceSafeStr(bookVO.getBalanceSafe());
			return R.data(resp);
		}

		return R.data(resp);
	}

	@PostMapping("/findBook")
	@ApiOperation(value = "账户明细-合计", notes = "账户明细-合计")
	@ApiLog("账户明细-合计")
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<BookCountResp> findBook(@RequestBody BaseUserIdReq req) {
		AccessToken accessToken = SecurityUtil.getLoginAccount();
		return R.data(apiService.getData(accessToken.getUserId(), accessToken.getPlatformId()));
	}

	@PostMapping("/getStatus")
	@ApiOperation(value = "账户明细-状态", notes = "账户明细-状态")
	@ApiLog("账户明细-状态")
	public R<List<BaseStatusResp>> getStatus() {
		List<BaseStatusResp> retList = new ArrayList<BaseStatusResp>();
 		//从枚举中获取状态码
		for (BusTypeApiEnums typeApiEnums : BusTypeApiEnums.values()) {
			retList.add(BaseStatusResp.builder().code(typeApiEnums.getVal()).str(typeApiEnums.getName()).build());
		}
		return R.data(retList);
	}

	@PostMapping("/findBookFlow")
	@ApiOperation(value = "账户明细-查询流水列表", notes = "账户明细-查询流水列表")
	@ApiLog("账户明细-查询流水列表")
	public R<Page<BookFlowApiResp>> findBookFlow(@RequestBody @Validated BookApiReq req) {
		if( req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		AccessToken accessToken = SecurityUtil.getLoginAccount();
		return R.data(apiService.findPage(req, accessToken.getUserId(), accessToken.getPlatformId()));
	}

}
