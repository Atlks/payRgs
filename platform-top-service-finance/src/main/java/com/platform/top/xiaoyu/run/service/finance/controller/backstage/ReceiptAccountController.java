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
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccount.ReceiptAccountInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccount.ReceiptAccountPageReq;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

/**
 * 收款账号 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_RECEIPT)
@Api(value = "收付款账号", tags = "收款账号")
@BackstageController
@MenuDefine(name = "收付款账号", moduleName = "ReceiptAccount", parentCode = "financeManage")
public class ReceiptAccountController extends TopBaseController {
	ReceiptAccountController(){}
	@Value("${top.fastdfs.url}")
	private String fastDfsUrl2="";
	@Autowired
	private IReceiptAccountService receiptAccountService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<ReceiptAccountVO>> findPage(@RequestBody @Validated ReceiptAccountPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<ReceiptAccountVO> page = new Page<ReceiptAccountVO>(req.getPage(), req.getSize());
		ReceiptAccountVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		Page<ReceiptAccountVO> page1 = receiptAccountService.findPage(page, vo);


		//set abs path
		try{
			page1.getRecords().forEach(new Consumer<ReceiptAccountVO>() {
				@Override
				public void accept(ReceiptAccountVO rec) {
					if( !rec.getIconUrl().startsWith("htt"))
					{
						rec.setIconUrl(  fastDfsUrl2+ rec.getIconUrl());
						rec.setQrCodeUrl(  fastDfsUrl2+ rec.getQrCodeUrl()   );
					}

				}
			});
		}catch(Exception e ){
			e.printStackTrace();
		}


		return R.data(new PageResp(
			page1));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<ReceiptAccountVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req.getId() || req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		ReceiptAccountVO detail = receiptAccountService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId());

		//set abs path ati
		try{
			if( !detail.getIconUrl().startsWith("htt")) {
				detail.setIconUrl(fastDfsUrl2 + detail.getIconUrl());
				detail.setQrCodeUrl(fastDfsUrl2 + detail.getQrCodeUrl());
			}

		}catch(Exception e ){
			e.printStackTrace();
		}
		return R.data(
			detail);
	}

	@PostMapping("/insert")
	@ApiOperation(value = "新增数据", notes = "新增数据")
	@ApiLog("新增数据")
	@ButtonDefine(name = "新增数据", internal = InternalResource.ADMIN)
	public R insert(@RequestBody @Validated ReceiptAccountInsertReq req) {
		if(StringUtils.isEmpty(req.getBankCode())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_BANKCODE);
		}
		if(StringUtils.isEmpty(req.getBankName())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_BANKNAME);
		}
		if(StringUtils.isEmpty(req.getAccountNo())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ACCOUNTNO);
		}
		if(StringUtils.isEmpty(req.getAccountName())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ACCOUNTNAME);
		}
		if(null == req.getToolsId() || req.getToolsId().longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_TOOLSID);
		}
		if(StringUtils.isEmpty(req.getQrCodeUrl())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_QRCODE);
		}
		if(StringUtils.isEmpty(req.getIconUrl())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ICON);
		}

		ReceiptAccountVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		vo.setIsDeleted(CommonStatus.ENABLE.getValue());
		return R.data(receiptAccountService.insert(vo));
	}

	@PostMapping("/updateStatus")
	@ApiOperation(value = "启用、作废", notes = "启用、作废")
	@ApiLog("启用、作废")
	@ButtonDefine(name = "启用、作废", internal = InternalResource.ADMIN)
	public R updateStatus(@RequestBody @Validated IdReq req) {
		if( null == req || null ==  req.getId() || req.getId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(receiptAccountService.updateStatus(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/del")
	@ApiOperation(value = "删除数据", notes = "删除数据")
	@ApiLog("删除数据")
	@ButtonDefine(name = "删除数据", internal = InternalResource.ADMIN)
	public R del(@RequestBody List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(receiptAccountService.del(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
