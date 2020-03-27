package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeFlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safeflow.SafeFlowPageReq;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeFlowService;
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
 * 保险箱流水 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_SAFEFLOW)
@Api(value = "保险箱流水", tags = "保险箱流水")
@BackstageController
@MenuDefine(name = "保险箱流水", moduleName = "SafeFlow", parentCode = "financeManage")
public class SafeFlowController extends TopBaseController {

	@Autowired
	private ISafeFlowService iflowservice;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询保险箱流水list", notes = "分页查询保险箱流水list")
	@ApiLog("分页查询保险箱流水list")
	@ButtonDefine(name = "分页查询保险箱流水list", internal = InternalResource.ADMIN)
	public R<PageResp<SafeFlowVO>> findPage(@RequestBody @Validated SafeFlowPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<SafeFlowVO> page = new Page<SafeFlowVO>(req.getPage(), req.getSize());
		SafeFlowVO vo = BeanCopyUtils.copyBean(req, SafeFlowVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(iflowservice.findPage(page, vo)));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询保险箱流水明细", notes = "查询保险箱流水明细")
	@ApiLog("查询保险箱流水明细")
	@ButtonDefine(name = "查询保险箱流水明细", internal = InternalResource.ADMIN)
	public R<SafeFlowVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || null == req.getId() || req.getId() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(iflowservice.findDetailId(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
