package com.platform.top.xiaoyu.run.service.pay.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.enums.PayPlatformKeyEnums;
import com.platform.top.xiaoyu.run.service.api.pay.enums.PayTypeMangoEnums;
import com.platform.top.xiaoyu.run.service.api.pay.enums.PayTypeOkfEnums;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatform.*;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatformrelease.PayPlatformPushReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PlatformResp;
import com.platform.top.xiaoyu.run.service.pay.service.IPayPlatformService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 第三方支付平台 控制器
 *
 * @author xiaoyu
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.BACKSTAGE_PAYPLATFORM)
@Api(value = "第三方支付平台", tags = "第三方支付平台")
@BackstageController
@MenuDefine(name = "第三方支付平台", moduleName = "payPlatform", parentCode = "payManage")
public class PayPlatformController extends TopBaseController {

	@Autowired
	private IPayPlatformService iPayPlatformService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<PayPlatformVO>> findPage(@RequestBody @Validated PayPlatformPageReq req) {
		if( req.getPage() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SIZE);
		}
		Page<PayPlatformVO> page = new Page<PayPlatformVO>(req.getPage(), req.getSize());
		PayPlatformVO vo = BeanCopyUtils.copyBean(req, PayPlatformVO.class);
		return R.data(new PageResp(iPayPlatformService.findPage(page, vo)));
	}

	@PostMapping("/findListAll")
	@ApiOperation(value = "查询所有", notes = "查询所有")
	@ApiLog("查询所有")
	@ButtonDefine(name = "查询所有", internal = InternalResource.ADMIN)
	public R<List<PayPlatformVO>> findListAll(@RequestBody PayPlatformAllReq req ) {
		return R.data(iPayPlatformService.findListAll( BeanCopyUtils.copyBean(req, PayPlatformVO.class)));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<PayPlatformVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || null == req.getId() || req.getId() == 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}
		return R.data(iPayPlatformService.findDetail(req.getId()));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "单行新增", notes = "单行新增")
	@ApiLog("单行新增")
	@ButtonDefine(name = "单行新增", internal = InternalResource.ADMIN)
	public R insert(@RequestBody @Validated PayPlatformInsertReq req) {
		if( null == req) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL);
		}
		if(null == req.getEncryptEnums()) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ENCRYPT);
		}
		PayPlatformVO vo = BeanCopyUtils.copyBean(req, PayPlatformVO.class);
		vo.setCreateTimestamp(LocalDateTime.now());
		vo.setEncrypt(req.getEncryptEnums().getValue());
		vo.setPayPlatfromKey(req.getPayPlatfromKeyEnums().getValue());

		if(req.getPayPlatfromKeyEnums() == PayPlatformKeyEnums.MANGO) {
			if( null == PayTypeMangoEnums.getType(req.getPayType()) ) {
				throw new BizBusinessException(BasePayExceptionType.PARAM_KEY);
			}
			vo.setPayTypeStr( PayTypeMangoEnums.getType(req.getPayType()).getDesc());
		}

		if(req.getPayPlatfromKeyEnums() == PayPlatformKeyEnums.OKF) {
			if( null == PayTypeOkfEnums.getType(req.getPayType()) ) {
				throw new BizBusinessException(BasePayExceptionType.PARAM_KEY);
			}
			vo.setPayTypeStr( PayTypeOkfEnums.getType(req.getPayType()).getDesc());
		}
		return R.data(iPayPlatformService.insert(vo));
	}

	@PostMapping("/update")
	@ApiOperation(value = "单行修改", notes = "单行修改")
	@ApiLog("单行修改")
	@ButtonDefine(name = "单行修改", internal = InternalResource.ADMIN)
	public R update(@RequestBody @Validated PayPlatformUpdateReq req) {
		if( null == req) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL);
		}
		if( req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}

		PayPlatformVO vo = BeanCopyUtils.copyBean(req, PayPlatformVO.class);
		vo.setUpdateTimestamp(LocalDateTime.now());
		if(null != req.getEncryptEnums()) {
			vo.setEncrypt(req.getEncryptEnums().getValue());
		}

		return R.data(iPayPlatformService.update(vo));
	}

	@PostMapping("/delBatch")
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@ApiLog("批量删除")
	@ButtonDefine(name = "批量删除", internal = InternalResource.ADMIN)
	public R delBatch(@RequestBody @Validated List<Long> ids) {
		return R.data(iPayPlatformService.delBatch(ids));
	}

	@PostMapping("/push")
	@ApiOperation(value = "发布支付平台", notes = "发布支付平台")
	@ApiLog("发布支付平台")
	@ButtonDefine(name = "发布支付平台", internal = InternalResource.ADMIN)
	public R push(@RequestBody @Validated PayPlatformPushReq req) {
		return R.data(iPayPlatformService.push(req));
	}

	@PostMapping("/cancelPush")
	@ApiOperation(value = "取消发布", notes = "取消发布")
	@ApiLog("取消发布")
	@ButtonDefine(name = "取消发布", internal = InternalResource.ADMIN)
	public R cancelPush(@RequestBody @Validated PayPlatformPushReq req) {
		return R.data(iPayPlatformService.cancelPush(req));
	}

	@PostMapping("/getPayTypeCombox")
	@ApiOperation(value = "获取支付方式", notes = "获取支付方式")
	@ApiLog("获取支付方式")
	@ButtonDefine(name = "获取支付方式", internal = InternalResource.ADMIN)
	public R<List<PlatformResp>> getPayTypeCombox(@RequestBody @Validated PayTypeReq req) {
		List<PlatformResp> list = Lists.newArrayList();
		if(req.getPlatformKey() == PayPlatformKeyEnums.MANGO ) {
			for (PayTypeMangoEnums value : PayTypeMangoEnums.values()) {
				PlatformResp resp = new PlatformResp();
				resp.setValue(String.valueOf(value.getValue()));
				resp.setName(value.getDesc());
				list.add(resp);
			}
		}
		if( req.getPlatformKey() == PayPlatformKeyEnums.OKF ) {
			for (PayTypeOkfEnums value : PayTypeOkfEnums.values()) {
				PlatformResp resp = new PlatformResp();
				resp.setValue(String.valueOf(value.getValue()));
				resp.setName(value.getDesc());
				list.add(resp);
			}
		}
		return R.data(list);
	}

	@PostMapping("/getPayPlatformCombox")
	@ApiOperation(value = "获取支付平台", notes = "获取支付平台")
	@ApiLog("获取支付平台")
	@ButtonDefine(name = "获取支付平台", internal = InternalResource.ADMIN)
	public R<List<PlatformResp>> getPayPlatformCombox() {
		List<PlatformResp> list = Lists.newArrayList();
		for (PayPlatformKeyEnums value : PayPlatformKeyEnums.values()) {
			PlatformResp resp = new PlatformResp();
			resp.setValue(value.getValue());
			resp.setName(value.getName());
			list.add(resp);
		}
		return R.data(list);
	}

}
