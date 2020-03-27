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
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeRateVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.safe.SafePageReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.saferate.SafeRateInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.saferate.SafeRateUpdateReq;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeRateService;
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

import java.util.List;

/**
 * 保险箱利率配置 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_SAFERATE)
@Api(value = "保险箱利率配置", tags = "保险箱利率配置")
@BackstageController
@MenuDefine(name = "保险箱利率配置", moduleName = "SafeRate", parentCode = "financeManage")
public class SafeRateController extends TopBaseController {

	@Autowired
	private ISafeRateService rateService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@ApiLog("分页查询")
	@ButtonDefine(name = "分页查询", internal = InternalResource.ADMIN)
	public R<PageResp<SafeRateVO>> findPage(@RequestBody @Validated SafePageReq req) {
		if( req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if( req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<SafeRateVO> page = new Page<SafeRateVO>(req.getPage(), req.getSize());
		SafeRateVO vo = BeanCopyUtils.copyBean(req, SafeRateVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(new PageResp(rateService.findPage(page, vo)));
	}

	@PostMapping("/findDetail")
	@ApiOperation(value = "查询明细", notes = "查询明细")
	@ApiLog("查询明细")
	@ButtonDefine(name = "查询明细", internal = InternalResource.ADMIN)
	public R<SafeRateVO> findDetail(@RequestBody @Validated IdReq req) {
		if (null == req.getId() || req.getId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		SafeRateVO vo = BeanCopyUtils.copyBean(req, SafeRateVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(rateService.findDetail(vo));
	}

	@PostMapping("/update")
	@ApiOperation(value = "更新", notes = "更新")
	@ApiLog("更新")
	@ButtonDefine(name = "更新", internal = InternalResource.ADMIN)
	public R update(@RequestBody @Validated SafeRateUpdateReq req) {
		if (null == req.getId() || req.getId() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		if (null == req.getDayNum() || req.getDayNum() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_DAYNUM);
		}
		if (null == req.getRate() || req.getRate().doubleValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RATE);
		}
		SafeRateVO vo = BeanCopyUtils.copyBean(req, SafeRateVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		return R.data(rateService.update(vo));
	}

	@PostMapping("/insert")
	@ApiOperation(value = "新增配置", notes = "新增配置")
	@ApiLog("新增配置")
	@ButtonDefine(name = "新增配置", internal = InternalResource.ADMIN)
	public R insert(@RequestBody SafeRateInsertReq req) {
		if (null == req.getDayNum() || req.getDayNum() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_DAYNUM);
		}
		if (null == req.getRate() || req.getRate().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RATE);
		}
		SafeRateVO vo = SafeRateVO.builder().dayNum(req.getDayNum()).platformId(SecurityUtil.getLoginAccount().getPlatformId())
			.rate(req.getRate()).build();
		return R.data(rateService.insert(vo));
	}

	@PostMapping("/delBatch")
	@ApiOperation(value = "删除数据", notes = "删除数据")
	@ApiLog("删除数据")
	@ButtonDefine(name = "删除数据", internal = InternalResource.ADMIN)
	public R delBatch(@RequestBody List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(rateService.del(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
