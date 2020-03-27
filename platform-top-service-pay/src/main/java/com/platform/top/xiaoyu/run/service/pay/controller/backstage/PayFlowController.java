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
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayFlowVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payflow.PayFlowPageReq;
import com.platform.top.xiaoyu.run.service.pay.service.IPayFlowService;
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

/**
 * 支付流水 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.BACKSTAGE_FLOW)
@Api(value = "支付流水", tags = "支付流水")
@BackstageController
@MenuDefine(name = "支付流水", moduleName = "PayFlow", parentCode = "payManage")
public class PayFlowController extends TopBaseController {

	@Autowired
	private IPayFlowService flowService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<PayFlowVO>> findPage(@RequestBody @Validated PayFlowPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SIZE);
		}
		Page<PayFlowVO> page = new Page<PayFlowVO>(req.getPage(), req.getSize());
		PayFlowVO vo = BeanCopyUtils.copyBean(req, PayFlowVO.class);
		vo.setPlatformId( SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(flowService.findPage(page, vo)));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<PayFlowVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || null == req.getId() || req.getId() == 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}
		return R.data(flowService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
