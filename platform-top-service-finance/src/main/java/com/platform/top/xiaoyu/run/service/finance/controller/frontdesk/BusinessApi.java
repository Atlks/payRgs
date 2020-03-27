package com.platform.top.xiaoyu.run.service.finance.controller.frontdesk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.PageReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusStatusEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.FsPayStatusEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.business.BusinessDetailResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.business.BusinessFlowResp;
import com.platform.top.xiaoyu.run.service.finance.service.IBusRechargeService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.FrontdeskController;
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
 * 资金流水 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.FRONTDESK_BUSINESS)
@Api(value = "资金流水", tags = "资金流水")
@FrontdeskController
public class BusinessApi extends TopBaseController {

	@Autowired
	private IBusRechargeService rechargeService;

	@PostMapping("/findFlow")
	@ApiOperation(value = "流水详情", notes = "流水详情")
	@ApiLog("流水详情")
	public R<PageResp<BusinessFlowResp>> findFlow(@RequestBody @Validated PageReq req) {
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

		//查询状态为：成功  所有充值记录
		vo.setPayStatus(FsPayStatusEnums.PAY_OK.getVal());
		page = rechargeService.findPage(page, vo);

		Page<BusinessFlowResp> ret_page = new Page<BusinessFlowResp>(page.getPages(), page.getSize(), page.getTotal());
		ret_page.setRecords(BeanCopyUtils.copyList(page.getRecords(), BusinessFlowResp.class));

		List<BusinessVO> listBusVO = page.getRecords();
		if (!CollectionUtils.isEmpty(listBusVO)) {
			List<BusinessFlowResp> retlistBusVO = new ArrayList<BusinessFlowResp>();
			for (BusinessVO businessVO : listBusVO) {
				BusinessFlowResp respVO = BeanCopyUtils.copyBean(businessVO, BusinessFlowResp.class);
				if (null != respVO.getStatusCode() && BusStatusEnums.getType(respVO.getStatusCode()) == BusStatusEnums.CODE_SYN_OK) {
					respVO.setActualAmountStr(businessVO.getActualAmount());
				}
				respVO.setAmountStr(businessVO.getAmount());
				retlistBusVO.add(respVO);
			}
			ret_page.setRecords(retlistBusVO);
		}

		return R.data(new PageResp(ret_page));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "资金明细", notes = "资金明细")
	@ApiLog("资金明细")
	public R<PageResp<BusinessDetailResp>> findDetail(@RequestBody @Validated PageReq req) {
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

		//查询状态为：成功所有 充值记录
		vo.setPayStatus(FsPayStatusEnums.PAY_OK.getVal());
		page = rechargeService.findPage(page, vo);

		Page<BusinessDetailResp> ret_page = new Page<BusinessDetailResp>(page.getPages(), page.getSize(), page.getTotal());

		List<BusinessVO> listBusVO = page.getRecords();
		if (!CollectionUtils.isEmpty(listBusVO)) {
			List<BusinessDetailResp> retlistBusVO = new ArrayList<BusinessDetailResp>();
			for (BusinessVO businessVO : listBusVO) {
				BusinessDetailResp respVO = BeanCopyUtils.copyBean(businessVO, BusinessDetailResp.class);
				respVO.setActualAmountStr(businessVO.getActualAmount());
				retlistBusVO.add(respVO);
			}
			ret_page.setRecords(retlistBusVO);
		}
		return R.data(new PageResp(ret_page));
	}

}
