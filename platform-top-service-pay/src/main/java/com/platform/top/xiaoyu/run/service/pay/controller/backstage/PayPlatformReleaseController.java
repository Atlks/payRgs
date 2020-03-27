package com.platform.top.xiaoyu.run.service.pay.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformReleaseVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatform.PayPlatformAllReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatform.PayPlatformPageReq;
import com.platform.top.xiaoyu.run.service.pay.service.IPayPlatformReleaseService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 查看平台支付列表 控制器
 *
 * @author xiaoyu
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.BACKSTAGE_PAYPLATFORMRELEASE)
@Api(value = "查看平台支付列表", tags = "查看平台支付列表")
@BackstageController
@MenuDefine(name = "查看平台支付列表", moduleName = "payPlatformRelease", parentCode = "payManage")
public class PayPlatformReleaseController extends TopBaseController {

	@Autowired
	private IPayPlatformReleaseService releaseService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<PayPlatformReleaseVO>> findPage(@RequestBody @Validated PayPlatformPageReq req) {
		if( req.getPage() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SIZE);
		}
		Page<PayPlatformReleaseVO> page = new Page<PayPlatformReleaseVO>(req.getPage(), req.getSize());
		PayPlatformReleaseVO vo = BeanCopyUtils.copyBean(req, PayPlatformReleaseVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(releaseService.findPage(page, vo)));
	}

	@PostMapping("/findListAll")
	@ApiOperation(value = "查询所有", notes = "查询所有")
	@ApiLog("查询所有")
	@ButtonDefine(name = "查询所有", internal = InternalResource.ADMIN)
	public R<List<PayPlatformReleaseVO>> findListAll(@RequestBody PayPlatformAllReq req ) {
		PayPlatformReleaseVO vo = BeanCopyUtils.copyBean(req, PayPlatformReleaseVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(releaseService.findListAll(vo));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<PayPlatformReleaseVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || null == req.getId() || req.getId() == 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}
		return R.data(releaseService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/delBatch")
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@ApiLog("批量删除")
	@ButtonDefine(name = "批量删除", internal = InternalResource.ADMIN)
	public R delBatch(@RequestBody @Validated List<Long> ids) {
		return R.data(releaseService.delBatch(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
