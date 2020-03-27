package com.platform.top.xiaoyu.run.service.pay.constant;

/**
 *
 * @author coffey
 */
public interface TableNameConstant {

	/**
	 * pay_whitelist_t  白名单
	 */
	String WHITELIST_TABLENAME = "pay_whitelist_t";

	/**
	 * pay_tools_t 用户支付接口等级积分配置
	 */
	String TOOLSPAY_TABLENAME = "pay_tools_t";

	/**
	 * pay_user_lvl_t 用户等级积分表
	 */
	String USERLVL_TABLENAME = "pay_user_lvl_t";

	/**
	 * pay_timeout_t 超时配置表
	 */
	String TIMEOUT_TABLENAME = "pay_timeout_t";

	/**
	 * pay_token_t 安全认证token表
	 */
	String TOKEN_TABLENAME = "pay_token_t";

	/**
	 * pay_flow_t 支付流水表
	 */
	String PAYFLOW_TABLENAME = "pay_flow_t";

	/**
	 * pay_info_t 支付记录表
	 */
	String PAYINFO_TABLENAME = "pay_info_t";

	/**
	 * pay_platform_t 第三方支付平台表
	 */
	String PLATFORM_TABLENAME = "pay_platform_t";

	/**
	 * pay_platform_release_t 支付平台发布表
	 */
	String PLATFORMRELEASE_TABLENAME = "pay_platform_release_t";

}
