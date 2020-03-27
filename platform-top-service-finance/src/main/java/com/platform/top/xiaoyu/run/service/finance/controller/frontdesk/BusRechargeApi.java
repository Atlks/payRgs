package com.platform.top.xiaoyu.run.service.finance.controller.frontdesk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.AccessToken;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTradingMannerEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.receiptaccounttools.ReceiptAccountToolsAllReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeAllReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeInsertOnlineReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargePageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeAccountResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeOnlineResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeTypeResp;
import com.platform.top.xiaoyu.run.service.finance.service.IBusRechargeService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.FRONTDESK_RECHARGE)
@Api(value = "充值", tags = "充值")
@FrontdeskController
public class BusRechargeApi extends TopBaseController {

	@Autowired
	private IBusRechargeService rechargeService;
	@Autowired
	private IReceiptAccountToolsService toolsService;
	@Autowired
	private IReceiptAccountService accountService;

	@PostMapping("/findRechargePage")
	@ApiOperation(value = "分页查询充值记录List", notes = "分页查询充值记录List")
	@ApiLog("分页查询充值记录List")
	public R<PageResp<RechargeResp>> findRechargePage(@RequestBody @Validated RechargePageReq req) {
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

		page = rechargeService.findPage(page, vo);
		Page<RechargeResp> ret_page = new Page<RechargeResp>(page.getPages(), page.getSize(), page.getTotal());
		ret_page.setRecords(BeanCopyUtils.copyList(page.getRecords(), RechargeResp.class));
		return R.data(new PageResp(ret_page));
	}

	@PostMapping("/findRechargeListAll")
	@ApiOperation(value = "查询充值记录ListAll", notes = "查询充值记录ListAll")
	@ApiLog("查询充值记录ListAll")
	public R<List<RechargeResp>> findRechargeListAll(@RequestBody @Validated RechargeAllReq req) {
		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(BeanCopyUtils.copyList(rechargeService.findListAll(vo), RechargeResp.class));
	}

	@PostMapping("/findRechargeDetail")
	@ApiOperation(value = "查询充值记录明细", notes = "查询充值记录明细")
	@ApiLog("查询充值记录明细")
	public R<RechargeResp> findRechargeDetail(@RequestBody @Validated IdReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(BeanCopyUtils.copyBean(rechargeService.findDetailId(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()), RechargeResp.class));
	}

	@PostMapping("/insertOnlineRe")
	@ApiOperation(value = "提交充值", notes = "提交充值")
	@ApiLog("提交充值")
	public R<RechargeOnlineResp> insertOnlineRe(@RequestBody @Validated RechargeInsertOnlineReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}

		if( null == req.getAccountId() || req.getAccountId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ACTID);
		}
		BusTradingMannerEnums enums = BusTradingMannerEnums.getType(req.getTradingManner());
		if(enums == BusTradingMannerEnums.BUS_TRA_BANK_FORWARD || enums == BusTradingMannerEnums.BUS_TRA_YSF_FORWARD ) {
			if(StringUtil.isEmpty(req.getPayRemarks())) {
				throw new BizBusinessException(BaseExceptionType.PARAM_RECHARGE_NAME_NULL);
			} else {
				if(req.getPayRemarks().length() > 200) {
					throw new BizBusinessException(BaseExceptionType.PARAM_LENGT);
				}
			}
		} else if(enums == BusTradingMannerEnums.BUS_TRA_WX_PAY || enums == BusTradingMannerEnums.BUS_TRA_ZFB_PAY
				|| enums == BusTradingMannerEnums.BUS_TRA_BANK_RECHARGE) {
			//默认支付用户名
			req.setPayRemarks("财务系统在线支付");
		}

		AccessToken accessToken = SecurityUtil.getLoginAccount();
		return R.data(rechargeService.insertOnline(req, accessToken.getUserId(), accessToken.getPlatformId()));
	}

	@PostMapping("/findToolsTypeListAll")
	@ApiOperation(value = "查询充值类型", notes = "查询充值类型")
	@ApiLog("查询充值类型")
	public R<List<ReceiptAccountToolsVO>> findToolsTypeListAll() {
		List<ReceiptAccountToolsVO> typelist = toolsService.findListALL(ReceiptAccountToolsVO.builder().platformId(SecurityUtil.getLoginAccount().getPlatformId()).build());
		List<ReceiptAccountToolsVO> retList = new ArrayList<ReceiptAccountToolsVO>();
		if(!CollectionUtils.isEmpty(typelist)) {
			//去重设值
			Map<Integer, ReceiptAccountToolsVO> map = new HashMap<Integer, ReceiptAccountToolsVO>();
			for (ReceiptAccountToolsVO vo : typelist) {
				if(null == map.get(vo.getType())){
					map.put(vo.getType(), vo);
				}
			}
			//迭代出所有的配置数据, 放入容器
			for (Integer typeKey : map.keySet()) {
				retList.add(map.get(typeKey));
			}
		}
		return R.data(retList);
	}

	@PostMapping("/findToolsListAll")
	@ApiOperation(value = "查询充值方式", notes = "查询充值方式")
	@ApiLog("查询充值方式")
	public R<List<RechargeTypeResp>> findToolsListAll(@RequestBody @Validated ReceiptAccountToolsAllReq req) {
		if ( null == req.getId() || req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		ReceiptAccountToolsVO vo = BeanCopyUtils.copyBean(req, ReceiptAccountToolsVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		//查询所有类型的充值
		vo.setId(null);
		List<ReceiptAccountToolsVO> toolsVOS = toolsService.findListALL(vo);
		if(CollectionUtils.isEmpty(toolsVOS)) {
			return R.fail("暂无数据");
		}
		return R.data(toolsService.findCombox(toolsVOS, SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/findActListAll")
	@ApiOperation(value = "查询收款账号All", notes = "查询收款账号All")
	@ApiLog("查询收款账号All")
	public R<List<RechargeAccountResp>> findActListAll(@RequestBody @Validated IdReq req) {
		if(null == req || req.getId().longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		ReceiptAccountVO vo = ReceiptAccountVO.builder().platformId(SecurityUtil.getLoginAccount().getPlatformId()).toolsId(req.getId()).build();
		return R.data(BeanCopyUtils.copyList(accountService.findListALL(vo), RechargeAccountResp.class));
	}

}
