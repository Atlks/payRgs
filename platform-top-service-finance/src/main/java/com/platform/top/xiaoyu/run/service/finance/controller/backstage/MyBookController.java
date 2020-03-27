package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.BaseUserIdReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.BaseUserIdUserNameReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.PwdInitReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.PwdUpdateReq;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.chwin.firefighting.apiserver.net.RequestUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的账本 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_BOOK)
@Api(value = "我的账本", tags = "我的账本")
@BackstageController
@MenuDefine(name = "我的账本", moduleName = "MyBook", parentCode = "financeManage")
public class MyBookController extends TopBaseController {

	@Autowired
	private IBookService bookService;

	@PostMapping("/findMyBook")
	@ApiOperation(value = "查询我的账本余额", notes = "查询我的账本余额")
	@ApiLog("查询我的账本余额")
	@ButtonDefine(name = "查询我的账本余额", internal = InternalResource.ADMIN)
	public R<BookVO> findMyBook(@RequestBody @Validated BaseUserIdUserNameReq req) {
		if ( StringUtil.isEmpty(req.getUserName()) && (null == req.getUserId() || req.getUserId() <= 0) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_FAIL);
		}
		BookVO bookVO = null;
		if ( !StringUtil.isEmpty(req.getUserName()) ) {
			bookVO = BookVO.builder().userName(req.getUserName()).build();
		} else {
			bookVO = BookVO.builder().userId(req.getUserId()).build();
		}

		bookVO.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(bookService.findDetail(bookVO));
	}

	@PostMapping("/findMyBookFail")
	@ApiOperation(value = "查询我的账本余额，不存在抛异常", notes = "查询我的账本余额，不存在抛异常")
	@ApiLog("查询我的账本余额，不存在抛异常")
	@ButtonDefine(name = "查询我的账本余额，不存在抛异常", internal = InternalResource.ADMIN)
	public R<BookVO> findMyBookFail(@RequestBody @Validated BaseUserIdUserNameReq req) {
		if ( StringUtil.isEmpty(req.getUserName()) && (null == req.getUserId() || req.getUserId() <= 0) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_FAIL);
		}
		BookVO bookVO = null;
		if ( !StringUtil.isEmpty(req.getUserName()) ) {
			bookVO = BookVO.builder().userName(req.getUserName()).build();
		} else {
			bookVO = BookVO.builder().userId(req.getUserId()).build();
		}

		bookVO.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		bookVO = bookService.findDetail(bookVO);
		if ( null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		return R.data(bookVO);
	}


	@PostMapping("/updatePayPwd")
	@ApiOperation(value = "修改支付密码", notes = "修改支付密码")
	@ApiLog("修改支付密码")
	@ButtonDefine(name = "修改支付密码", internal = InternalResource.ADMIN)
	public R<Boolean> updatePayPwd(@RequestBody @Validated PwdUpdateReq req) {
		if ( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		return R.data(bookService.updatePwd(req.getUserId(), SecurityUtil.getLoginAccount().getPlatformId(), req.getPwd(), req.getNewPwd()));
	}

	@PostMapping("/updateInitPwd")
	@ApiOperation(value = "首次设置支付密码", notes = "首次设置支付密码")
	@ApiLog("首次设置支付密码")
	@ButtonDefine(name = "首次设置支付密码", internal = InternalResource.ADMIN)
	public R<Boolean> updateInitPwd(@RequestBody @Validated PwdInitReq req) {
		if ( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		return R.data(bookService.updateInitPwd(req.getUserId(), SecurityUtil.getLoginAccount().getPlatformId(), req.getPwd()));
	}
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
@Autowired
	HttpServletRequest reqx;
	@PostMapping("/findPayPwd")
	@ApiOperation(value = "查询是否设置过支付密码", notes = "查询是否设置过支付密码")
	@ApiLog("查询是否设置过支付密码")
	@ButtonDefine(name = "查询是否设置过支付密码", internal = InternalResource.ADMIN)
	public R<Boolean> findPayPwd(@RequestBody @Validated BaseUserIdReq req) {
		if ( null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		BookVO bookVO = bookService.findDetail(req.getUserId(), SecurityUtil.getLoginAccount().getPlatformId());
		if ( null != bookVO ) {
			if (!StringUtil.isEmpty(bookVO.getExtractPwd())) {
				return R.data(true);
			}
		}
		return R.data(false);
	}


	@RequestMapping   ("/balanceHistoryList")
	@ApiOperation(value = "统计列表", notes = "统计列表")
	@ApiLog("统计列表")
	@ButtonDefine(name = "统计列表", internal = InternalResource.ADMIN)

	public R<List> balanceHistoryList() {

		return R.data(sqlSessionTemplate.selectList( RequestUtil.getMap(reqx).get("selectid").toString() , RequestUtil.getMap(reqx)));
	}


}
