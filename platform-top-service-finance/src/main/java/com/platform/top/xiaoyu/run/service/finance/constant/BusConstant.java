package com.platform.top.xiaoyu.run.service.finance.constant;

/**
 *
 * @author coffey
 */
public interface BusConstant {

	public static final String UTILS_DIVISOR = "1000000";

	public static final String FAIL = "系统出错";
	public static final String RECHARGE_MANUAL_REVIEW = "人工充值审核通过";
	public static final String RECHARGE_ONLINE_REVIEW = "在线充值审核通过";
	public static final String RECHARGE_ONLINE = "在线充值";
	public static final String SAFE_IN = "保险箱转入";
	public static final String SAFE_OUT = "保险箱转出";
	public static final String SAFE_CALE = "保险箱利息结算";
	public static final String EXTRACT_MANUAL = "人工出账";
	public static final String EXTRACT_MANUAL_FAIL = "人工出账拒绝";
	public static final String EXTRACT_MANUAL_OK = "人工出账通过，冻结金额扣减且核销";
	public static final String EXTRACT_SYS = "在线提现系统出账";
	public static final String SIGNIN = "登入游戏";
	public static final String SIGNOUT = "登出游戏";
	public static final String EXTRACT_MONEY = "提现冻结金额减少";
	public static final String EXTRACT_MONEY_FAIL = "支付失败==>提现冻结金额减少,可用余额增加";
	public static final String FREEZE_SIGNIN = "登入冻结";
	public static final String FREEZE_EXTRACT = "人工提现冻结";
	public static final String FREEZE_EXTRACT_SYS = "用户申请提现冻结";
	public static final String VERIFY_FREEZE = "完成核销金额";
	public static final String BALANCE_COMMISSION = "已领取佣金金额，可用金额已增加";
	public static final String OPERATE = "领取洗码， 可用金额累加";
	public static final String VIP = " ， 用户会员， 可用金额累加";
	public static final String ACTIVITY_REGUSER = "用户注册赠送礼金， 可用金额累加";

}
