package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeInsertNotOnlineReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargePageReq;
import com.platform.top.xiaoyu.run.service.finance.service.IBusRechargeService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
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
 * 审核存款记录 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_RECHARGE)
@Api(value = "审核存款", tags = "审核存款")
@BackstageController
@MenuDefine(name = "审核存款", moduleName = "BusRecharge", parentCode = "financeManage")
public class BusRechargeController extends TopBaseController {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	BusRechargeController(){}
//	@Value("${top.fastdfs.url}")
//	private String fastDfsUrl;
	@Autowired
	private IBusRechargeService rechargeService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询存款记录", notes = "分页查询存款记录")
	@ApiLog("分页查询存款记录")
	@ButtonDefine(name = "分页查询存款记录", internal = InternalResource.ADMIN)
	public R<PageResp<BusinessVO>> findPage(@RequestBody @Validated RechargePageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<BusinessVO> page = new Page<BusinessVO>(req.getPage(), req.getSize());
		BusinessVO vo = BeanCopyUtils.copyBean(req, BusinessVO.class);
		vo.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		Page<BusinessVO> page1 = rechargeService.findPage(page, vo);


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

		return R.data(new PageResp(
			page1));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询存款记录明细", notes = "查询存款记录明细")
	@ApiLog("查询存款记录明细")
	@ButtonDefine(name = "查询存款记录明细", internal = InternalResource.ADMIN)
	public R<BusinessVO> findDetail(@RequestBody @Validated IdReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		BusinessVO detailId = rechargeService.findDetailId(req.getId(), SecurityUtil.getLoginAccount().getPlatformId());
		//set abs path ati
//		try{
//			detailId.setFileUrl( fastDfsUrl + detailId.getFileUrl() );
//
//		}catch(Exception e ){
//			e.printStackTrace();
//		}
		return R.data(
			detailId);
	}

	@PostMapping("/udpateOk")
	@ApiOperation(value = "存款审核-强制存款成功", notes = "存款审核-强制存款成功")
	@ApiLog("存款审核-强制存款成功")
	@ButtonDefine(name = "存款审核-强制存款成功", internal = InternalResource.ADMIN)
	public R udpateOk(@RequestBody @Validated IdReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(rechargeService.updateOk(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/insertNotOnline")
	@ApiOperation(value = "人工存款", notes = "人工存款")
	@ApiLog("人工存款")
	@ButtonDefine(name = "人工存款", internal = InternalResource.ADMIN)
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R insertNotOnline(@RequestBody @Validated RechargeInsertNotOnlineReq req) {
		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		} else {
			//转成金额
			BigDecimal amount = new BigDecimal(req.getAmount());
			if( null == amount || amount.longValue() <= 0 ) {
				throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
			}
		}
		if( null == req.getUserId() || req.getUserId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		if( null == req.getAccountId() || req.getAccountId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ACTID);
		}
		if(StringUtils.isEmpty(req.getName())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAYNAME_NULL);
		}
//line 412   BusRechargeServiceImpl
		return R.data(rechargeService.insertNotOnline(req.getUserId(), SecurityUtil.getLoginAccount().getPlatformId(), req.getAmount(), req.getName(), req.getAccountId()));
	}

	@PostMapping("/updateFail")
	@ApiOperation(value = "存款审核-强制存款失败", notes = "存款审核-强制存款失败")
	@ApiLog("存款审核-强制存款失败")
	@ButtonDefine(name = "存款审核-强制存款失败", internal = InternalResource.ADMIN)
	public R updateFail(@RequestBody @Validated IdReq req) {
		if( null == req.getId() || req.getId().longValue() <= 0 ){
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(rechargeService.updateFail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
