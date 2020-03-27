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
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractNotOnlineInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractPageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractReviewManualInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractReviewSysInsertReq;
import com.platform.top.xiaoyu.run.service.finance.service.IBusExtractService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * 审核出款 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_EXTRACT)
@Api(value = "审核出款", tags = "审核出款")
@BackstageController
@MenuDefine(name = "审核出款", moduleName = "BusExtract", parentCode = "financeManage")
public class BusExtractController extends TopBaseController {

	BusExtractController(){}

	@Autowired
	private IBusExtractService extractService;

//	@Value("${top.fastdfs.url}")
//	public String fastDfsUrl;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<BusinessVO>> findPage(@RequestBody @Validated ExtractPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<BusinessVO> page = new Page<BusinessVO>(req.getPage(), req.getSize());
		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());


		Page<BusinessVO> page1 = extractService.findPage(page, vo);
		//set abs path
//		try{
//			page1.getRecords().forEach(new Consumer<BusinessVO>() {
//				@Override
//				public void accept(BusinessVO bankBindingVO) {
//					bankBindingVO.setFileUrl( fastDfsUrl + bankBindingVO.getFileUrl() );
//				}
//			});
//		}catch(Exception e ){
//			e.printStackTrace();
//		}


		return R.data(new PageResp(page1));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<BusinessVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || req.getId().longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		BusinessVO detailId = extractService.findDetailId(req.getId(), SecurityUtil.getLoginAccount().getPlatformId());

		//set abs path ati
//		try{
//			detailId.setFileUrl( fastDfsUrl + detailId.getFileUrl() );
//
//		}catch(Exception e ){
//			e.printStackTrace();
//		}

		return R.data(detailId);
	}

	@PostMapping("/udpateStatussOk")
	@ApiOperation(value = "审核成功出款-人工转账", notes = "审核成功出款-人工转账")
	@ApiLog("审核成功出款-人工转账")
	@ButtonDefine(name = "审核成功出款-人工转账", internal = InternalResource.ADMIN)
	public R<Boolean> udpateStatussOk(@RequestBody @Validated ExtractReviewManualInsertReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(extractService.updateStatusOK(req, SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/udpateStatussSysOk")
	@ApiOperation(value = "审核成功出款-系统自动转账", notes = "审核成功出款-系统自动转账")
	@ApiLog("审核成功出款-系统自动转账")
	@ButtonDefine(name = "审核成功出款-系统自动转账", internal = InternalResource.ADMIN)
	public R<Boolean> udpateStatussSysOk(@RequestBody @Validated ExtractReviewSysInsertReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}

		if( null == req.getPayPlatformId() || req.getPayPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAYPLATFORMID_NULL);
		}
		return R.data(extractService.updateStatusOkSys(req.getId(), SecurityUtil.getLoginAccount().getPlatformId(), req.getPayPlatformId()));
	}

	@PostMapping("/insertNotOnline")
	@ApiOperation(value = "申请人工录入取款", notes = "申请人工录入取款")
	@ApiLog("申请人工录入取款")
	@ButtonDefine(name = "申请人工录入取款", internal = InternalResource.ADMIN)
	public R<Boolean> insertNotOnline(@RequestBody @Validated ExtractNotOnlineInsertReq req) {
		if( null == req.getUserId() || req.getUserId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ACTID);
		}
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}
		//转成金额
		BigDecimal amount = new BigDecimal(req.getAmount());
		if( null == amount || amount.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}
		if( null == req.getTradingManner() || req.getTradingManner().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_TRADINGMANNER_NULL);
		}
		if( null == req.getAccountId() || req.getAccountId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ACTID);
		}
		return R.data(extractService.insertNotOnline(req, SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/udpateStatussFail")
	@ApiOperation(value = "拒绝出款", notes = "拒绝出款")
	@ApiLog("拒绝出款")
	@ButtonDefine(name = "拒绝出款", internal = InternalResource.ADMIN)
	public R udpateStatussFail(@RequestBody @Validated IdReq req) {
		if(null == req || req.getId().longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(extractService.updateStatusFail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
