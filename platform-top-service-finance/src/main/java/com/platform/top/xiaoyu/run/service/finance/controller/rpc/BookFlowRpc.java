package com.platform.top.xiaoyu.run.service.finance.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.feign.IBookFlowFeignClient;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.BookFlowRpcReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookFlowRpcResp;
import com.platform.top.xiaoyu.run.service.finance.service.IFlowService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账本流水 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.RPC_BUSINESS)
@Api(value = "账本流水", tags = "账本流水")
@RpcController
public class BookFlowRpc extends TopBaseController implements IBookFlowFeignClient {

	@Autowired
	private IFlowService flowService;

	@PostMapping("/findListAll")
	@ApiOperation(value = "查询账本数据list", notes = "查询账本数据list")
	@ApiLog("查询账本数据list")
	@Override
	public R<List<BookFlowRpcResp>> findListAll(BookFlowRpcReq req) {
		if(null == req.getBeginDate()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_BOOK_BEGINDATE);
		}
		if(null == req.getEndDate()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_BOOK_ENDDATE);
		}
		List<FlowVO> list = flowService.findFlowListAll(req.getPlatformId(), req.getIds(), req.getBeginDate(), req.getEndDate());

		return R.data(BeanCopyUtils.copyList(list, BookFlowRpcResp.class));

	}

	@PostMapping("/findListCount")
	@ApiOperation(value = "查询账本数据所有数据行计总", notes = "查询账本数据所有数据行计总")
	@ApiLog("查询账本数据所有数据行计总")
	@Override
	public R<Integer> findListCount(BookFlowRpcReq req) {
		if(null == req.getBeginDate()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_BOOK_BEGINDATE);
		}
		if(null == req.getEndDate()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_BOOK_ENDDATE);
		}

		return R.data(flowService.findFlowListCount(req.getPlatformId(), req.getIds(), req.getBeginDate(), req.getEndDate()));

	}

}

