package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 账本流水实体类
 * @author coffey
 */
@Data
@TableName(TableNameConstant.FLOW_TABLENAME)
@ApiModel(value = "Flow", description = "Flow")
public class Flow extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

    /**  预交易金额 */
    public static final String P_AMOUNT = "amount";
	/**  实际交易金额 */
    public static final String P_ACTUAL_AMOUNT = "actual_amount";
	/**  大类 : 1 会员充值、2 系统可用余额累加、3 活动-可用余额累加、4 可用余额累减、5 可用提现 */
	public static final String P_TYPE_ALL = "type_all";
	/**  类型 : 1 登入、2 登出、3 充值、4 提现、5 人工取款、6 会员取款、7 领取佣金、8 红包、9 保险箱记录、10 结算入账、11 领取洗码、12 赠送彩金、13 活动优惠、14 人工存入、15 会员充值 */
	public static final String P_TYPE = "type";
	/**  交易单号表id */
	public static final String P_CODE_ID = "code_id";
	/**  交易单号 */
	public static final String P_CODE = "code";
	/**  支付系统 */
	public static final String P_SYS_KEY = "sys_key";
	/**  交易方式（  1. 银行转账 2. 云闪付转账 3.微信转账 4. 支付宝转账 5. 微信支付 6. 支付宝支付 7 代理充值 8 银联支付 ） */
	public static final String P_TRADING_MANNER = "trading_manner";
	/**  第三方平台单号（回调的支付单号） */
	public static final String P_CODE_OTHER = "code_other";
	/**  交易时间（从登入，登出，充值，提现，保险箱转入转出 等 类型中 传入的记录时间字段） */
	public static final String P_BUS_TIMESTAMP = "bus_timestamp";
	/**  付款人账号（银行、支付宝、微信、PayPal 等） */
	public static final String P_PAY_ACCOUNT = "pay_account";
	/**  付款人姓名 */
	public static final String P_PAY_NAME = "pay_name";
	/**  付款账号所属支行code */
	public static final String P_PAY_BANK_CODE = "pay_bank_code";
	/**  付款账号所属支行 */
	public static final String P_PAY_BANK = "pay_bank";
	/**  收款人账号（银行、支付宝、微信、PayPal 等） */
	public static final String P_RECEIPT_ACCOUNT = "receipt_account";
	/**  收款人姓名 */
	public static final String P_RECEIPT_NAME = "receipt_name";
	/**  收款账号所属支行code */
	public static final String P_RECEIPT_BANK_CODE = "receipt_bank_code";
	/**  收款账号所属支行 */
	public static final String P_RECEIPT_BANK = "receipt_bank";
	/**  交易描述 */
	public static final String P_DESCRIPTION = "description";
	/**  记录时间（当前新增记录的时间） */
	public static final String P_CREATE_TIMESTAMP = "create_timestamp";
	/**  数据行备注 */
	public static final String P_REMARK = "remark";
	/**  游戏type */
	public static final String P_GAME_TYPE = "game_type";;
	/**  游戏name */
	public static final String P_GAME_NAME = "game_name";
	/**  交易方式str  */
	public static final String P_TRADING_MANNER_STR = "trading_manner_str";
	/**  付款信息，卡号后四位、姓名  */
	public static final String P_PAY_REMARKS = "pay_remarks";
	/**  可用余额  */
	public static final String P_BALANCE = "balance";
	/**  收入  */
	public static final String P_BALANCE_IN = "balance_in";
	/**  支出  */
	public static final String P_BALANCE_OUT = "balance_out";
	/**  状态str  */
	public static final String P_STATUSS_STR = "statuss_str";
	/**  类型str  */
	public static final String P_TYPE_STR = "type_str";
	/**  大类str  */
	public static final String P_TYPE_ALL_STR = "type_all_str";


	@ApiModelProperty(value = "类型str")
	@TableField(value = P_TYPE_STR)
	private String typeStr;

	@ApiModelProperty(value = "大类str")
	@TableField(value = P_TYPE_ALL_STR)
	private String typeAllStr;

	@ApiModelProperty(value = "状态str")
	@TableField(value = P_STATUSS_STR)
	private String statussStr;

    @ApiModelProperty(value = "预交易金额")
    @TableField(value = P_AMOUNT)
    private String amount;

	@ApiModelProperty(value = "实际交易金额")
	@TableField(value = P_ACTUAL_AMOUNT)
	private String actualAmount;

	@ApiModelProperty(value = "大类： 1 会员充值、2 系统可用余额累加、3 活动-可用余额累加、4 可用余额累减、5 可用提现")
	@TableField(value = P_TYPE_ALL)
	private Integer typeAll;

	@ApiModelProperty(value = "类型 : 1 登入、2 登出、3 充值、4 提现、5 人工取款、6 会员取款、7 领取佣金、8 红包、9 保险箱记录、10 结算入账、11 领取洗码、12 赠送彩金、13 活动优惠、14 人工存入、15 会员充值 ")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "交易单号表id")
	@TableField(value = P_CODE_ID)
	private Long codeId;

	@ApiModelProperty(value = "交易单号")
	@TableField(value = P_CODE)
	private String code;

	@ApiModelProperty(value = "支付系统")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "交易方式（ 1. 银行转账 2. 云闪付转账 3.微信转账 4. 支付宝转账 5. 微信支付 6. 支付宝支付 7 代理充值 8 银联支付 ）")
	@TableField(value = P_TRADING_MANNER)
	private Integer tradingManner;

	@ApiModelProperty(value = "第三方平台单号（回调的支付单号）")
	@TableField(value = P_CODE_OTHER)
	private String codeOther;

	@ApiModelProperty(value = "交易时间（从登入，登出，充值，提现，保险箱转入转出 等 类型中 传入的记录时间字段）")
	@TableField(value = P_BUS_TIMESTAMP)
	private LocalDateTime busTimestamp;

	@ApiModelProperty(value = "付款人账号（银行、支付宝、微信、PayPal 等）")
	@TableField(value = P_PAY_ACCOUNT)
	private String payNo;

	@ApiModelProperty(value = "付款人姓名")
	@TableField(value = P_PAY_NAME)
	private String payName;

	@ApiModelProperty(value = "付款账号所属支行code")
	@TableField(value = P_PAY_BANK_CODE)
	private String payBankCode;

	@ApiModelProperty(value = "付款账号所属支行")
	@TableField(value = P_PAY_BANK)
	private String payBank;

	@ApiModelProperty(value = "收款人账号（银行、支付宝、微信、PayPal 等）")
	@TableField(value = P_RECEIPT_ACCOUNT)
	private String receiptNo;

	@ApiModelProperty(value = "收款人姓名")
	@TableField(value = P_RECEIPT_NAME)
	private String receiptName;

	@ApiModelProperty(value = "收款账号所属支行code")
	@TableField(value = P_RECEIPT_BANK_CODE)
	private String receiptBankCode;

	@ApiModelProperty(value = "收款账号所属支行")
	@TableField(value = P_RECEIPT_BANK)
	private String receiptBank;

	@ApiModelProperty(value = "交易描述")
	@TableField(value = P_DESCRIPTION)
	private String description;

	@ApiModelProperty(value = "数据行备注")
	@TableField(value = P_REMARK)
	private String remark;

	@ApiModelProperty(value = "游戏type")
	@TableField(value = P_GAME_TYPE)
	private String gameType;

	@ApiModelProperty(value = "游戏name")
	@TableField(value = P_GAME_NAME)
	private String gameName;

	@ApiModelProperty(value = "交易方式str")
	@TableField(value = P_TRADING_MANNER_STR)
	private String tradingMannerStr;

	@ApiModelProperty(value = "付款信息，卡号后四位、姓名")
	@TableField(value = P_PAY_REMARKS)
	private String payRemarks;

	@ApiModelProperty(value = "可用余额")
	@TableField(value = P_BALANCE)
	private String balance;

	@ApiModelProperty(value = "收入")
	@TableField(value = P_BALANCE_IN)
	private String balanceIn;

	@ApiModelProperty(value = "支出")
	@TableField(value = P_BALANCE_OUT)
	private String balanceOut;

}
