package com.platform.top.xiaoyu.run.service.finance.job;

import com.platform.top.xiaoyu.run.service.finance.service.ISafeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "financeSafeJob")
@Component
public class SafeJob extends IJobHandler {

	@Autowired
	private ISafeService iSafeService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("当前参数:{}", param);
		XxlJobLogger.log("执行计算 保险金利率任务 reg");

		//计算利息
		iSafeService.calcJob();

		XxlJobLogger.log("执行计算 保险金利率任务 end");
		return SUCCESS;
	}
}
