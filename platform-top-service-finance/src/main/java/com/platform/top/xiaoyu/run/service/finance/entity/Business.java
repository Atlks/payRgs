package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.RECHARGE_TABLENAME)
@ApiModel(value = "Business", description = "Business")
public class Business extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

//	状态：101 入账核查中、102 入账成功、103 入账失败、104 入账取消
//            201 正在出款、202 成功出款、203 退回出款、204 拒绝出款

	/**  充值金额  */
	public static final String P_AMOUNT = "amount";
	/**  实际交易金额  */
	public static final String P_ACTUAL_AMOUNT = "actual_amount";
	/**  实际交易时间  */
	public static final String P_PAY_TIMESTAMP = "pay_timestamp";
	/**  支付系统key : mango 芒果支付 */
	public static final String P_SYS_KEY = "sys_key";
	/**  大类 1 会员充值、2 系统可用余额累加、3 活动-可用余额累加、4 可用余额累减、5 可用提现  */
	public static final String P_TYPE_ALL = "type_all";
	/**  类型：1 登入、2 登出、3 充值、4 提现、5 人工取款、6 会员取款、7 领取佣金、8 红包、9 保险箱记录、10 结算入账、11 领取洗码、12 赠送彩金、13 活动优惠、14 人工存入、15 会员充值  */
	public static final String P_TYPE = "type";
	/**  交易方式：  1. 银行转账 2. 云闪付转账 3.微信转账 4. 支付宝转账 5. 微信支付 6. 支付宝支付 7 代理充值 8 银联支付 */
	public static final String P_TRADING_MANNER = "trading_manner";
	/**  付款账号  */
	public static final String P_PAY_ACCOUNT = "pay_account";
	/**  付款账号所属支行code  */
	public static final String P_PAY_BANK_CODE = "pay_bank_code";
	/**  付款账号所属支行  */
	public static final String P_PAY_BANK = "pay_bank";
	/**  付款人名字  */
	public static final String P_PAY_NAME = "pay_name";
	/**  收款账号  */
	public static final String P_RECEIPT_ACCOUNT = "receipt_account";
	/**  收款人名字  */
	public static final String P_RECEIPT_NAME = "receipt_name";
	/**  收款账号所属支行code  */
	public static final String P_RECEIPT_BANK_CODE = "receipt_bank_code";
	/**  收款账号所属支行  */
	public static final String P_RECEIPT_BANK = "receipt_bank";
	/**  交易单号  */
	public static final String P_CODE = "code";
	/**  支付编码  */
	public static final String P_PAY_CODE = "pay_code";
	/**  支付状态： 1 成功 2 失败 3 等待 */
	public static final String P_PAY_STATUS = "pay_status";
	/**  更新人  */
	public static final String P_UPDATE_BY = "update_by";
	/**  更新人ID  */
	public static final String P_UPDATE_BY_ID = "update_by_id";
	/**  支付凭证附件URL  */
	public static final String P_FILE_URL = "file_url";
	/**  支付凭证附件URL  */
	public static final String P_STATUSS_STR = "statuss_str";
	/**  支付凭证附件URL  */
	public static final String P_TYPE_ALL_STR = "type_all_str";
	/**  支付凭证附件URL  */
	public static final String P_TYPE_STR = "type_str";
	/**  支付凭证附件URL  */
	public static final String P_PAY_STATUS_STR = "pay_status_str";
	/**  交易方式str  */
	public static final String P_TRADING_MANNER_STR = "trading_manner_str";
	/**  付款信息，卡号后四位，姓名  */
	public static final String P_PAY_REMARKS = "pay_remarks";
	/**  打码状态 0 未同步 1 满足 2 不满足  */
	public static final String P_STATUS_CODE = "status_code";
	/**  打码状态 0 未同步 1 满足 2 不满足  */
	public static final String P_STATUS_CODE_STR = "status_code_str";
	/**  打码洗码比率  */
	public static final String P_ORDER_CODE_RATE = "order_code_rate";

	@ApiModelProperty(value = "用户id")
	@TableField(value = P_USER_ID)
	private Long userId;

	@ApiModelProperty(value = "充值金额")
	@TableField(value = P_AMOUNT)
	private String amount;

	@ApiModelProperty(value = "实际交易金额")
	@TableField(value = P_ACTUAL_AMOUNT)
	private String actualAmount;

	@ApiModelProperty(value = "实际交易时间")
	@TableField(value = P_PAY_TIMESTAMP)
	private LocalDateTime payTimestamp;

	@ApiModelProperty(value = "大类")
	@TableField(value = P_TYPE_ALL)
	private Integer typeAll;

	@ApiModelProperty(value = "类型")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "交易方式（ 1. 银行转账 2. 云闪付转账 3.微信转账 4. 支付宝转账 5. 微信支付 6. 支付宝支付 7 代理充值 8 银联支付 ）")
	@TableField(value = P_TRADING_MANNER)
	private Integer tradingManner;

	@ApiModelProperty(value = "付款账号")
	@TableField(value = P_PAY_ACCOUNT)
	private String payAccount;

	@ApiModelProperty(value = "付款账号所属支行CODE")
	@TableField(value = P_PAY_BANK_CODE)
	private String payBankCode;

	@ApiModelProperty(value = "付款账号所属支行")
	@TableField(value = P_PAY_BANK)
	private String payBank;

	@ApiModelProperty(value = "付款人名字")
	@TableField(value = P_PAY_NAME)
	private String payName;

	@ApiModelProperty(value = "收款账号")
	@TableField(value = P_RECEIPT_ACCOUNT)
	private String receiptAccount;

	@ApiModelProperty(value = "收款人名字")
	@TableField(value = P_RECEIPT_NAME)
	private String receiptName;

	@ApiModelProperty(value = "收款账号所属支行CODE")
	@TableField(value = P_RECEIPT_BANK_CODE)
	private String receiptBankCode;

	@ApiModelProperty(value = "收款账号所属支行")
	@TableField(value = P_RECEIPT_BANK)
	private String receiptBank;

	@ApiModelProperty(value = "交易单号")
	@TableField(value = P_CODE)
	private Long code;

	@ApiModelProperty(value = "支付单号")
	@TableField(value = P_PAY_CODE)
	private Long payCode;

	@ApiModelProperty(value = "支付状态： 1 成功 2 失败")
	@TableField(value = P_PAY_STATUS)
	private Integer payStatus;

	@ApiModelProperty(value = "更新人")
	@TableField(value = P_UPDATE_BY)
	private String updateBy;

	@ApiModelProperty(value = "更新人ID")
	@TableField(value = P_UPDATE_BY_ID)
	private Long updateById;

	@ApiModelProperty(value = "支付系统")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "支付凭证附件URL")
	@TableField(value = P_FILE_URL)
	private String fileUrl;

	@ApiModelProperty(value = "状态str")
	@TableField(value = P_STATUSS_STR)
	private String statussStr;

	@ApiModelProperty(value = "大类str")
	@TableField(value = P_TYPE_ALL_STR)
	private String typeAllStr;

	@ApiModelProperty(value = "类型str")
	@TableField(value = P_TYPE_STR)
	private String typeStr;

	@ApiModelProperty(value = "支付状态str")
	@TableField(value = P_PAY_STATUS_STR)
	private String payStatusStr;

	@ApiModelProperty(value = "交易方式str")
	@TableField(value = P_TRADING_MANNER_STR)
	private String tradingMannerStr;

	@ApiModelProperty(value = "付款信息、姓名、卡号后四位")
	@TableField(value = P_PAY_REMARKS)
	private String payRemarks;

	@ApiModelProperty(value = "打码状态 0 未同步 1 满足 2 不满足")
	@TableField(value = P_STATUS_CODE)
	private Integer statusCode;

	@ApiModelProperty(value = "打码状态 0 未同步 1 满足 2 不满足")
	@TableField(value = P_STATUS_CODE_STR)
	private String statusCodeStr;

	@ApiModelProperty(value = "打码、洗码比率")
	@TableField(value = P_ORDER_CODE_RATE)
	private BigDecimal orderCodeRate;

}
