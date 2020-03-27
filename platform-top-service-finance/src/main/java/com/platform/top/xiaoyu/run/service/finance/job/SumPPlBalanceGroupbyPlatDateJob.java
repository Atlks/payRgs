package com.platform.top.xiaoyu.run.service.finance.job;

import com.platform.top.xiaoyu.run.service.finance.service.ISafeService;
import com.platform.top.xiaoyu.run.service.finance.service.impl.BalanceServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value = "SumPPlBalanceGroupbyPlatDateJob")
@Component
public class SumPPlBalanceGroupbyPlatDateJob extends IJobHandler {

	@Autowired
	BalanceServiceImpl BalanceServiceImpl1;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("当前参数:{}", param);
		XxlJobLogger.log("执行计算 SumPPlBalanceGroupbyPlatDateJob start");

		//计算利息
		// iSafeService.calcJob();
		//if(new Date().getHours()>23)
			BalanceServiceImpl1.sumPplSumBalanceDateGrupbyPlat();

		XxlJobLogger.log("执行计算  SumPPlBalanceGroupbyPlatDateJob  end");
		return SUCCESS;
	}
}
