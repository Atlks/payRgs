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
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountValueVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccount.ReceiptAccountPageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccountvalue.ReceiptAccountValueInsertReq;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountValueService;
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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款账号 配置值 控制器
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_ACTVALUE)
@Api(value = "收款账号设置值", tags = "收款账号设置值")
@BackstageController
@MenuDefine(name = "收款账号设置值", moduleName = "ReceiptAccountValue", parentCode = "financeManage")
public class ReceiptAccountValueController extends TopBaseController {

	@Autowired
	private IReceiptAccountValueService valueService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<Integer>> findPage(@RequestBody @Validated ReceiptAccountPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<ReceiptAccountValueVO> page = new Page<ReceiptAccountValueVO>(req.getPage(), req.getSize());
		ReceiptAccountValueVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountValueVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		page = valueService.findPage(page, vo);

		Page<Integer> retPage = new Page<Integer>(page.getCurrent(), page.getSize());
		if (!CollectionUtils.isEmpty(page.getRecords())) {
			List<Integer> valueList = new ArrayList<Integer>();
			for (ReceiptAccountValueVO valueVO : page.getRecords()) {
				valueList.add(valueVO.getValue());
			}
			retPage.setRecords(valueList);
		}


		return R.data(new PageResp(retPage));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<ReceiptAccountValueVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req.getId() || req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		return R.data(valueService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@ApiLog("新增数据")
	@ButtonDefine(name = "新增数据", internal = InternalResource.ADMIN)
	public R insert(@RequestBody @Validated ReceiptAccountValueInsertReq req) {
		if( CollectionUtils.isEmpty(req.getList()) ) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		if(null == req.getToolsId() || req.getToolsId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_TOOLSID);
		}
		return R.data(valueService.insertBatch(req.getToolsId(), SecurityUtil.getLoginAccount().getPlatformId(), req.getList()));
	}

	@PostMapping("/del")
	@ApiOperation(value = "删除数据", notes = "删除数据")
	@ApiLog("删除数据")
	@ButtonDefine(name = "删除数据", internal = InternalResource.ADMIN)
	public R del(@RequestBody List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(valueService.del(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
