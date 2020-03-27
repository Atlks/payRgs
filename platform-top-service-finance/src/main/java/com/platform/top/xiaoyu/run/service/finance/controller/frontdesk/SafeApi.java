package com.platform.top.xiaoyu.run.service.finance.controller.frontdesk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.AccessToken;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.enums.SafeFlowEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeFlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafeInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafeUpdatePwdReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safeflow.SafeFlowApiPageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safeflow.SafeFlowInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.safe.SafeFlowResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.safe.SafeResp;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeFlowService;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeService;
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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的保险箱 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.FRONTDESK_SAFE)
@Api(value = "我的保险箱", tags = "我的保险箱")
@FrontdeskController
public class SafeApi extends TopBaseController {

	@Autowired
	private ISafeService safeService;
	@Autowired
	private ISafeFlowService iflowservice;

	@PostMapping("/findSafeDetail")
	@ApiOperation(value = "查询我的保险箱Detail", notes = "查询我的保险箱Detail")
	@ApiLog("查询我的保险箱Detail")
	public R<SafeResp> findSafeDetail() {
		SafeVO safeVO = safeService.findDetailUserId(SecurityUtil.getLoginAccount().getUserId(), SecurityUtil.getLoginAccount().getPlatformId());
		SafeResp resp = BeanCopyUtils.copyBean(safeVO, SafeResp.class);
		if(null != resp) {
			BookVO bookVO = safeService.findBalance(SecurityUtil.getLoginAccount().getUserId(), SecurityUtil.getLoginAccount().getPlatformId());
			resp.setBalanceSafeStr(bookVO.getBalanceSafe());
			resp.setBalanceStr(bookVO.getBalance());
		}
		return R.data(resp);
	}

	@PostMapping("/updatePwd")
	@ApiOperation(value = "修改保险箱密码", notes = "修改保险箱密码")
	@ApiLog("修改保险箱密码")
	public R updatePwd(@RequestBody @Validated SafeUpdatePwdReq req) {
		return R.data(safeService.updatePwd(SecurityUtil.getLoginAccount().getUserId(), req.getPwd(), req.getNewPwd(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "初始化设置保险箱密码", notes = "初始化设置保险箱密码")
	@ApiLog("初始化设置保险箱密码")
	public R insert(@RequestBody @Validated SafeInsertReq req) {
		if(StringUtils.isEmpty(req.getPwd())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD);
		}
		SafeVO vo = BeanCopyUtils.copyBean(req, SafeVO.class);
		vo.setUserId (SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(safeService.insert(vo));
	}

	@PostMapping("/sign")
	@ApiOperation(value = "保险箱登录", notes = "保险箱登录")
	@ApiLog("保险箱登录")
	public R sign(@RequestBody @Validated SafeInsertReq req) {
		if(StringUtils.isEmpty(req.getPwd())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD);
		}
		SafeVO vo = BeanCopyUtils.copyBean(req, SafeVO.class);
		vo.setUserId (SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(safeService.sign(vo));
	}

	@PostMapping("/findSafe")
	@ApiOperation(value = "检测是否设置密码", notes = "检测是否设置密码")
	@ApiLog("检测是否设置密码")
	public R<Boolean> findSafe() {
		if (null != safeService.findDetailUserId(SecurityUtil.getLoginAccount().getUserId(), SecurityUtil.getLoginAccount().getPlatformId())) {
			return R.data(true);
		}
		return R.data(false);
	}

	@PostMapping("/updateTransferIn")
	@ApiOperation(value = "转入", notes = "转入")
	@ApiLog("转入")
	public R updateTransferIn(@RequestBody @Validated SafeFlowInsertReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}
		AccessToken token = SecurityUtil.getAccessToken();
		SafeFlowVO vo = SafeFlowVO.builder().userId(token.getUserId()).platformId(token.getPlatformId()).amount(req.getAmount().toString()).build();

		return R.data(iflowservice.insertFlowIn(vo));
	}

	@PostMapping("/updateTransferOut")
	@ApiOperation(value = "转出", notes = "转出")
	@ApiLog("转出")
	public R updateTransferOut(@RequestBody @Validated SafeFlowInsertReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}
		AccessToken token = SecurityUtil.getAccessToken();
		SafeFlowVO vo = SafeFlowVO.builder().userId(token.getUserId()).platformId(token.getPlatformId()).amount(String.valueOf(req.getAmount())).build();

		return R.data(iflowservice.insertFlowOut(vo));
	}


	@PostMapping("/updateTransfer")
	@ApiOperation(value = "转入转出", notes = "转入转出")
	@ApiLog("转入转出")
	public R updateTransfer(@RequestBody @Validated SafeFlowInsertReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}
		if( null == req.getType() || req.getType().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_TYPE);
		}
		AccessToken token = SecurityUtil.getAccessToken();
		SafeFlowVO vo = SafeFlowVO.builder().userId(token.getUserId()).platformId(token.getPlatformId()).amount(String.valueOf(req.getAmount())).build();

		if(req.getType().intValue() == SafeFlowEnums.TYPE_IN.getVal().intValue()) {
			return R.data(iflowservice.insertFlowIn(vo));
		} else if(req.getType().intValue() == SafeFlowEnums.TYPE_OUT.getVal().intValue()) {
			return R.data(iflowservice.insertFlowOut(vo));
		}
		return R.fail(BusConstant.FAIL);
	}

	@PostMapping("/findSafeFlowPage")
	@ApiOperation(value = "分页明细list", notes = "分页明细list")
	@ApiLog("分页明细list")
	public R<PageResp<SafeFlowResp>> findSafeFlowPage(@RequestBody @Validated SafeFlowApiPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<SafeFlowVO> page = new Page<SafeFlowVO>(req.getPage(), req.getSize());
		SafeFlowVO vo = BeanCopyUtils.copyBean(req, SafeFlowVO.class);
		vo.setUserId(SecurityUtil.getLoginAccount().getUserId());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		page = iflowservice.findPage(page, vo);

		Page<SafeFlowResp> ret_page = new Page<SafeFlowResp>(page.getCurrent(), page.getSize());

		List<SafeFlowVO> listBusVO = page.getRecords();
		if (!CollectionUtils.isEmpty(listBusVO)) {
			List<SafeFlowResp> retlistBusVO = new ArrayList<SafeFlowResp>();
			for (SafeFlowVO tempVO : listBusVO) {
				SafeFlowResp respVO = BeanCopyUtils.copyBean(tempVO, SafeFlowResp.class);

				respVO.setBalanceSafeStr(tempVO.getBalanceSafe());
				respVO.setBalanceStr(tempVO.getBalance());
				respVO.setTypeStr(SafeFlowEnums.getType(tempVO.getType()).getName());

				retlistBusVO.add(respVO);
			}
			ret_page.setRecords(retlistBusVO);
		}

		return R.data(new PageResp(ret_page));
	}

}
