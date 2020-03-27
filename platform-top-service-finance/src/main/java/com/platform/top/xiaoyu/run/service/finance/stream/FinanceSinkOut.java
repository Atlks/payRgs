package com.platform.top.xiaoyu.run.service.finance.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface FinanceSinkOut {

	public static void main(String[] args) {

	}

//	/** 用户注册 */
//	public String USER_TO_FINANCE_INPUT = "user_to_finance_input";
//	/** 佣金 */
//	public String AGENT_TO_FINANCE_INPUT = "agent_to_finance_input";
//	/** 洗码 */
//	public String OPERATE_TO_FINANCE_INPUT = "operate_to_finance_input";
//	/** 用户登出游戏， 可用余额累加 */
//	public String THREADGAME_TO_FINANCE_INPUT = "threadgame_to_finance_input";
//	/** 活动， 可用余额累加    def  channel*/
//	public String ACTIVITY_TO_FINANCE_INPUT = "activity_to_finance_input";
//
//
//
//
//	@Input(USER_TO_FINANCE_INPUT)
//	SubscribableChannel userToFinanceInput();
//	@Input(AGENT_TO_FINANCE_INPUT)
//	SubscribableChannel agentToFinanceInput();
//	@Input(OPERATE_TO_FINANCE_INPUT)
//	SubscribableChannel operateToFinanceInput();
//	@Input(THREADGAME_TO_FINANCE_INPUT)
//	SubscribableChannel threadgameToFinanceInput();



// 	@Output(FinanceSink. ACTIVITY_TO_FINANCE_INPUT)
//	MessageChannel activityToFinanceOut();



//	/** 活动， 可用余额累加 */
//	public String TOPIC_TEST = "TOPIC_TEST";
//	@Input(TOPIC_TEST)   //  "TOPIC_TEST"
//	SubscribableChannel SubscribableChannel_TOPIC_TEST();

}
