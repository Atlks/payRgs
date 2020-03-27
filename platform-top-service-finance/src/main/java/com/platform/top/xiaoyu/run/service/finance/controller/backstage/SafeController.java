package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.BaseIdUserIdReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafeCaleReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafePageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafeResetPwdReq;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 保险箱 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_SAFE)
@Api(value = "保险箱", tags = "保险箱")
@BackstageController
@MenuDefine(name = "保险箱", moduleName = "Safe", parentCode = "financeManage")
public class SafeController extends TopBaseController {

	private ISafeService iSafeService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<SafeVO>> findPage(@RequestBody @Validated SafePageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<SafeVO> page = new Page<SafeVO>(req.getPage(), req.getSize());
		SafeVO vo = BeanCopyUtils.copyBean(req, SafeVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(iSafeService.findPage(page, vo)));
	}

	@PostMapping("/updateReset")
	@ApiOperation(value = "保险箱重置密码", notes = "保险箱重置密码")
	@ApiLog("保险箱重置密码")
	@ButtonDefine(name = "保险箱重置密码", internal = InternalResource.ADMIN)
	public R updateReset(@RequestBody @Validated SafeResetPwdReq req) {
		if (null == req.getUserId() || req.getUserId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		if (StringUtils.isEmpty(req.getPwd())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD);
		}
		return R.data(iSafeService.updateReset(req.getUserId(), req.getPwd(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询保险箱数据明细", notes = "查询保险箱数据明细")
	@ApiLog("查询保险箱数据明细")
	@ButtonDefine(name = "查询保险箱数据明细", internal = InternalResource.ADMIN)
	public R<SafeVO> findDetail(@RequestBody @Validated BaseIdUserIdReq req) {
		return R.data(iSafeService.findDetailUserId(req.getUserId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/calc")
	@ApiOperation(value = "计算用户保险箱利息", notes = "计算用户保险箱利息")
	@ApiLog("计算用户保险箱利息")
	@ButtonDefine(name = "计算用户保险箱利息", internal = InternalResource.ADMIN)
	public R calc(@RequestBody @Validated SafeCaleReq req) {
		if(null == req.getToday()) {
			req.setToday(LocalDate.now());
		}
		if( req.getToday().isAfter(LocalDate.now()) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_DATE);
		}
		iSafeService.calcUser(req.getUserId(), req.getPlatformId(), req.getToday());
		return R.success("执行完成");
	}

}
