package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.platform.top.xiaoyu.run.service.api.auth.annotaions.ButtonDefine;
import com.platform.top.xiaoyu.run.service.api.auth.annotaions.MenuDefine;
import com.platform.top.xiaoyu.run.service.api.auth.enums.InternalResource;
import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 财务模块通用上传接口 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.BACKSTAGE_ACTTOOLS)
@Api(value = "财务模块通用上传接口", tags = "财务模块通用上传接口")
@BackstageController
@MenuDefine(name = "财务模块通用上传接口", moduleName = "fsUpload", parentCode = "financeManage")
public class FsFileUploadController extends TopBaseController {

	@PostMapping("/upload")
	@ApiOperation(value = "数据上传", notes = "数据上传")
	@ApiLog("数据上传")
	@ButtonDefine(name = "数据上传", internal = InternalResource.ADMIN)
	public R<String> uploadImage(@RequestParam("file") MultipartFile file) {
		if(file.isEmpty()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_FILE_NULL);
		}
		R ret = this.imageUpload(file);
		if(ret.isSuccess()) {
			return R.data(ret.getData()==null ? null : ret.getData().toString());
		}
		return R.fail("fail");
	}

}
