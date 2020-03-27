package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccounttools.ReceiptAccountToolsInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccounttools.ReceiptAccountToolsPageReq;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收款账号配置 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_ACTTOOLS)
@Api(value = "收款账号配置", tags = "收款账号配置")
@BackstageController
@MenuDefine(name = "收款账号配置", moduleName = "ReceiptAccountTools", parentCode = "financeManage")
public class ReceiptAccountToolsController extends TopBaseController {

	@Autowired
	private IReceiptAccountToolsService toolsService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<ReceiptAccountToolsVO>> findPage(@RequestBody @Validated ReceiptAccountToolsPageReq req) {
		if( req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<ReceiptAccountToolsVO> page = new Page<ReceiptAccountToolsVO>(req.getPage(), req.getSize());
		ReceiptAccountToolsVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountToolsVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		vo.setIsDeleted(CommonStatus.ENABLE.getValue());
		return R.data(new PageResp(toolsService.findPage(page, vo)));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<ReceiptAccountToolsVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req.getId() || req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		return R.data(toolsService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@ApiLog("新增数据")
	@ButtonDefine(name = "新增数据", internal = InternalResource.ADMIN)
	public R insert(@RequestBody @Validated ReceiptAccountToolsInsertReq req) {
		if(StringUtils.isEmpty(req.getName())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_BANKCODE);
		}
		if(null == req.getType() || req.getType().intValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_TYPE);
		}
		if(StringUtil.isEmpty(req.getPayPlatformId())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAYPLATFORMID_NULL);
		}
		ReceiptAccountToolsVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountToolsVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(toolsService.insert(vo));
	}

	@PostMapping("/del")
	@ApiOperation(value = "删除数据", notes = "删除数据")
	@ApiLog("删除数据")
	@ButtonDefine(name = "删除数据", internal = InternalResource.ADMIN)
	public R del(@RequestBody List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(toolsService.del(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
