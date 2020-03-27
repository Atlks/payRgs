package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.common.vo.req.IdReq;
import com.platform.top.xiaoyu.run.service.api.common.vo.resp.PageResp;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.bankbinding.BankBindingPageReq;
import com.platform.top.xiaoyu.run.service.finance.service.IBankBindingService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;

import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
//import org.chwin.firefighting.apiserver.data.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

/**
 * 绑定银行卡 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_BANDKBD)
@Api(value = "绑定银行卡", tags = "绑定银行卡")
@BackstageController
//@MenuDefine(name = "绑定银行卡", moduleName = "BankBinding", parentCode = "financeManage")
public class BankBindingController extends TopBaseController {

	public static void main(String[] args) {
		System.out.println( System.getProperty("spring.profiles.active"));
		System.out.println( System.getProperty("top.env"));

	//	BankBindingController bean = SpringUtil.getBean(BankBindingController.class);
//		BankBindingController bean = SpringUtil.get    (BankBindingController.class);
//		BankBindingPageReq pagereq=new BankBindingPageReq();
//		System.out.println(bean.findPage(pagereq  ));
	}

	BankBindingController(){}

	@Value("${top.fastdfs.url}")
	private String fastDfsUrl;

	@Autowired
	private IBankBindingService iBankBindingService;

	@PostMapping("/findPage")
	@ApiOperation(value = "分页查询绑定银行卡记录", notes = "分页查询绑定银行卡记录")
	@ApiLog("分页查询绑定银行卡记录")
	@ButtonDefine(name = "分页查询绑定银行卡记录", internal = InternalResource.ADMIN)
	public R<PageResp<BankBindingVO>> findPage(@RequestBody @Validated BankBindingPageReq req) {
		if(req.getPage() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAGE);
		}
		if(req.getSize() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_SIZE);
		}
		Page<BankBindingVO> page = new Page<BankBindingVO>(req.getPage(), req.getSize());
		BankBindingVO vo = BeanCopyUtils.copyBean(req, BankBindingVO.class);
		vo.setPlatformId(SecurityUtil.getLoginAccount().getPlatformId());
		Page<BankBindingVO> page1 = iBankBindingService.findPage(page, vo);

		//set abs path
		try{
			page1.getRecords().forEach(new Consumer<BankBindingVO>() {
				@Override
				public void accept(BankBindingVO bankBindingVO) {

					if( !bankBindingVO.getUrlIcon().startsWith("htt")) {
						bankBindingVO.setUrlBackroug(fastDfsUrl + bankBindingVO.getUrlBackroug());
						bankBindingVO.setUrlIcon(fastDfsUrl + "" + bankBindingVO.getUrlIcon());
					}
				}
			});
		}catch(Exception e ){
			e.printStackTrace();
		}

		return R.data(new PageResp(		page1));
	}

	@PostMapping("/updateCancel")
	@ApiOperation(value = "作废数据", notes = "作废数据")
	@ApiLog("作废数据")
	@ButtonDefine(name = "作废数据", internal = InternalResource.ADMIN)
	public R updateCancel(@RequestBody @Validated IdReq req) {
		if( null != req && req.getId() != null && req.getId() > 0 ) {
			return R.data(iBankBindingService.updateCancal(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
		}
		throw new BizBusinessException(BaseExceptionType.PARAM_ID);
	}

	@PostMapping("/updateEnable")
	@ApiOperation(value = "启用数据", notes = "启用数据")
	@ApiLog("启用数据")
	@ButtonDefine(name = "启用数据", internal = InternalResource.ADMIN)
	public R updateEnable(@RequestBody @Validated IdReq req) {
		if( null != req && req.getId() != null && req.getId() > 0 ) {
			return R.data(iBankBindingService.updateEnable(req.getId(), SecurityUtil.getLoginAccount().getPlatformId()));
		}
		throw new BizBusinessException(BaseExceptionType.PARAM_ID);
	}

	@PostMapping("/del")
	@ApiOperation(value = "删除数据", notes = "删除数据")
	@ApiLog("删除数据")
	@ButtonDefine(name = "删除数据", internal = InternalResource.ADMIN)
	public R del(@RequestBody @Validated List<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ID);
		}
		return R.data(iBankBindingService.del(ids, SecurityUtil.getLoginAccount().getPlatformId()));
	}

}
