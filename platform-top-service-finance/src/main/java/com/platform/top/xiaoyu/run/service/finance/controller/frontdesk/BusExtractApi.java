package com.platform.top.xiaoyu.run.service.finance.controller.frontdesk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BankBindingExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.bankbinding.BankBindingAllReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.bankbinding.BankBindingInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractApiInsertOnlineReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeAllReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargePageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.extract.ExtractResp;
import com.platform.top.xiaoyu.run.service.finance.service.IBankBindingService;
import com.platform.top.xiaoyu.run.service.finance.service.IBusExtractService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.FrontdeskController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 取款、提现、支付记录 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.FRONTDESK_EXTRACT)
@Api(value = "取款、提现、支付记录", tags = "取款、提现、支付记录")
@FrontdeskController
public class BusExtractApi extends TopBaseController {


	BusExtractApi(){}

	@Value("${top.fastdfs.url}")
	private String fastDfsUrl;

	@Autowired
	private IBusExtractService extractService;
	@Autowired
	private IBankBindingService iBankBindingService;

	@PostMapping("/insertOnlineEx")
	@ApiOperation(value = "提现", notes = "提现")
	@ApiLog("提现")
	public R<Boolean> insertOnline(@RequestBody @Validated ExtractApiInsertOnlineReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}
		if( null == req.getBangdingId() || req.getBangdingId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ACTID);
		}
		return R.data(extractService.insertOnline(req.getBangdingId(), SecurityUtil.getLoginAccount().getUserId(),
			SecurityUtil.getLoginAccount().getLoginPlatformId(), req.getAmount()));
	}

	@PostMapping("/findBankEnable")
	@ApiOperation(value = "查询用户是否已绑定银行卡", notes = "查询用户是否已绑定银行卡")
	@ApiLog("查询用户是否已绑定银行卡")
	public R<String> findBankEnable() {
		if ( null != iBankBindingService.findDetail(BankBindingVO.builder().platformId(SecurityUtil.getLoginAccount().
			getPlatformId()).userId(SecurityUtil.getLoginAccount().getUserId()).build())) {
			return R.success("存在绑定银行卡数据");
		}
		return R.fail("未查询到绑定银行卡数据");
	}



	@PostMapping("/findBankListAll")
	@ApiOperation(value = "查询银行卡list", notes = "查询银行卡list")
	@ApiLog("查询银行卡list")
	public R<List<BankBindingVO>> findBankListAll(@RequestBody @Validated BankBindingAllReq req) {
		BankBindingVO vo = BeanCopyUtils.copyBean(req, BankBindingVO.class);
		// todo
		vo.setUrlIcon(this.fastDfsUrl + "/"+ vo.getUrlIcon());
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(iBankBindingService.findListALL(vo));
	}

	@PostMapping("/updateIsDefault")
	@ApiOperation(value = "设置默认银行卡", notes = "设置默认银行卡")
	@ApiLog("设置默认银行卡")
	public R updateIsDefault(@RequestBody @Validated IdReq req) {
		if( null != req && req.getId() != null && req.getId() > 0 ) {
			return R.data(iBankBindingService.updateIsDefault(req.getId(), SecurityUtil.getLoginAccount().getUserId(), SecurityUtil.getLoginAccount().getPlatformId()));
		}
		throw new BizBusinessException(BaseExceptionType.PARAM_ID);
	}

	@PostMapping("/insertBank")
	@ApiOperation(value = "添加绑定银行卡", notes = "添加绑定银行卡")
	@ApiLog("添加绑定银行卡")
	public R<Boolean> insertBank(@RequestBody @Validated BankBindingInsertReq req) {

		if( StringUtils.isEmpty(req.getAccountName()) ) {
			throw new BizBusinessException(BankBindingExceptionType.PARAM_ACCOUNTNAME);
		}
		if( StringUtils.isEmpty(req.getAccountNo()) ) {
			throw new BizBusinessException(BankBindingExceptionType.PARAM_ACCOUNTNO);
		}
		if(null == req.getType() || req.getType().intValue() < 1) {
			throw new BizBusinessException(BankBindingExceptionType.PARAM_TYPE);
		} else {
			if(StringUtil.isEmpty(req.getBankCode())) {
				throw new BizBusinessException(BankBindingExceptionType.PARAM_BANK_CODE);
			}
		}

		BankBindingVO vo = BeanCopyUtils.copyBean(req, BankBindingVO.class);
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getAccessToken().getPlatformId());
		vo.setCreateBy(SecurityUtil.getAccessToken().getUserName());
		vo.setCreateUserId(SecurityUtil.getLoginAccount().getUserId());

		return R.data(iBankBindingService.insert(vo));
	}

	@PostMapping("/delBank")
	@ApiOperation(value = "删除绑定银行卡", notes = "删除绑定银行卡")
	@ApiLog("删除绑定银行卡")
	public R<Boolean> insertBank(@RequestBody @Validated IdReq req) {
		if(null == req.getId() || req.getId().longValue() < 1) {
			throw new BizBusinessException(BankBindingExceptionType.PARAM_TYPE);
		}
		List<Long> list = new ArrayList<Long>();
		list.add(req.getId());
		return R.data(iBankBindingService.del(list, SecurityUtil.getLoginAccount().getPlatformId()));
	}


	@PostMapping("/findExtractDetail")
	@ApiOperation(value = "查询交易流水列表数据", notes = "查询交易流水列表数据")
	@ApiLog("查询交易流水列表数据")
	public R<ExtractResp> findExtractDetail(@RequestBody @Validated IdReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(BeanCopyUtils.copyBean(extractService.findDetailId(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()), ExtractResp.class));
	}


	@PostMapping("/findExtractPage")
	@ApiOperation(value = "分页查询提现记录List", notes = "分页查询提现记录List")
	@ApiLog("分页查询提现记录List")
	public R<PageResp<ExtractResp>> findExtractPage(@RequestBody @Validated RechargePageReq req) {
		if( req.getPage() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<BusinessVO> page = new Page<BusinessVO>(req.getPage(), req.getSize());

		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);

		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		page = extractService.findPage(page, vo);
		Page<ExtractResp> ret_page = new Page<ExtractResp>(page.getPages(), page.getSize(), page.getTotal());
		ret_page.setRecords(BeanCopyUtils.copyList(page.getRecords(), ExtractResp.class));
		return R.data(new PageResp(ret_page));
	}

	@PostMapping("/findExtractListAll")
	@ApiOperation(value = "查询提现记录ListAll", notes = "查询提现记录ListAll")
	@ApiLog("查询提现记录ListAll")
	public R<List<ExtractResp>> findExtractListAll(@RequestBody @Validated RechargeAllReq req) {
		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(BeanCopyUtils.copyList(extractService.findListAll(vo), ExtractResp.class));
	}

	@PostMapping("/findExtractDeatil")
	@ApiOperation(value = "查询提现记录明细Detail", notes = "查询提现记录明细Detail")
	@ApiLog("查询提现记录明细Detail")
	public R<ExtractResp> findExtractDeatil(@RequestBody @Validated IdReq req) {
		if( req.getId() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(BeanCopyUtils.copyBean(extractService.findDetail(vo), ExtractResp.class));
	}



}
