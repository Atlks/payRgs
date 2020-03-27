package com.platform.top.xiaoyu.run.service.pay.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.UserLvlVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.userlvl.UserLvlAllReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.userlvl.UserLvlInsertReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.userlvl.UserLvlPageReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.userlvl.UserLvlUpdateReq;
import com.platform.top.xiaoyu.run.service.pay.service.IUserLvlService;
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

import java.util.List;

/**
 * 用户支付等级 控制器
 *
 * @author xiaoyu
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.BACKSTAGE_USERPAYLVL)
@Api(value = "用户支付接口等级", tags = "用户支付接口等级")
@BackstageController
@MenuDefine(name = "用户支付接口等级", moduleName = "UserLvl", parentCode = "payManage")
public class UserLvlController extends TopBaseController {

	@Autowired
	private IUserLvlService iUserLvlService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<UserLvlVO>> findPage(@RequestBody @Validated UserLvlPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SIZE);
		}
		Page<UserLvlVO> page = new Page<UserLvlVO>(req.getPage(), req.getSize());
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(iUserLvlService.findPage(page, vo)));
	}

	@PostMapping("/findListAll")
	@ApiOperation(value = "查询所有", notes = "查询所有")
	@ApiLog("查询所有")
	@ButtonDefine(name = "查询所有", internal = InternalResource.ADMIN)
	public R<List<UserLvlVO>> findListAll(@RequestBody @Validated UserLvlAllReq req) {
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(iUserLvlService.findListAll(vo));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<UserLvlVO> findDetail(@RequestBody @Validated IdReq req) {
		if(null == req || null == req.getId() || req.getId() == 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}
		return R.data(iUserLvlService.findDetail(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "单行新增", notes = "单行新增")
	@ApiLog("单行新增")
	@ButtonDefine(name = "单行新增", internal = InternalResource.ADMIN)
	public R insert(@RequestBody @Validated UserLvlInsertReq req) {
		if( null == req) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL);
		}
		if( req.getUserId().longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_USER_ID);
		}
		if( req.getLvl().intValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_LVL);
		}
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(iUserLvlService.insert(vo));
	}

	@PostMapping("/update")
	@ApiOperation(value = "单行修改", notes = "单行修改")
	@ApiLog("单行修改")
	@ButtonDefine(name = "单行修改", internal = InternalResource.ADMIN)
	public R update(@RequestBody @Validated UserLvlUpdateReq req) {
		if( null == req) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_NULL);
		}
		if( req.getId().longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ID);
		}
		if( req.getUserId().longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_USER_ID);
		}
		if( req.getLvl().intValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_LVL);
		}
		UserLvlVO vo = BeanCopyUtils.copyBean(req, UserLvlVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(iUserLvlService.update(vo));
	}

	@PostMapping("/delBatch")
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@ApiLog("批量删除")
	@ButtonDefine(name = "批量删除", internal = InternalResource.ADMIN)
	public R delBatch(@RequestBody @Validated List<Long> ids) {
		return R.data(iUserLvlService.delBatch(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}


}
