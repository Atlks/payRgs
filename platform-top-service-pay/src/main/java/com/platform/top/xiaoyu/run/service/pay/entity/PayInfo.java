package com.platform.top.xiaoyu.run.service.pay.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.pay.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付记录 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.PAYINFO_TABLENAME)
@ApiModel(value = "PayInfo", description = "支付记录")
public class PayInfo extends BaseEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 支付单号 */
	public static final String P_PAY_NO = "pay_no";
	/** 实际金额 */
	public static final String P_ACTUAL_MONEY = "actual_money";
	/** 预计金额 */
	public static final String P_REQ_MONEY = "req_money";
	/** 请求订单号 */
	public static final String P_REQ_ORDER_NO = "req_order_no";
	/** 请求系统备注 */
	public static final String P_REQ_REMARKS = "req_remarks";
	/** 自定义返回请求系统参数 */
	public static final String P_REQ_PARAM = "req_param";

	/** 支付状态 1.成功， 2.失败，3.等待 */
	public static final String P_STATUSS_PAY = "statuss_pay";
	/** 请求系统key */
	public static final String P_SYS_KEY = "sys_key";
	/** 请求系统名称 */
	public static final String P_SYS_NAME = "sys_name";
	/** 用户ID */
	public static final String P_USER_ID = "user_id";
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
	/**  类型： 1. 银行转账 2. 云闪付转账 3.1 微信转账 3.2 支付宝转账 4.1 微信支付 4.2 支付宝支付 5 代理充值  */
	public static final String P_TYPE = "type";
	/**  支付平台ID  */
	public static final String P_PLATFORM_PAY_ID = "platform_pay_id";
	/**  支付平台key  */
	public static final String P_PLATFORM_PAY_KEY = "platform_pay_key";
	/**  支付平台名称  */
	public static final String P_PLATFORM_PAY_NAME = "platform_pay_name";

	@ApiModelProperty(value = "支付单号")
	@TableField(value = P_PAY_NO)
	private Long payNo;

	@ApiModelProperty(value = "实际金额")
	@TableField(value = P_ACTUAL_MONEY)
	private String actualMoney;

	@ApiModelProperty(value = "金额")
	@TableField(value = P_REQ_MONEY)
	private String reqMoney;

	@ApiModelProperty(value = "订单号")
	@TableField(value = P_REQ_ORDER_NO)
	private String reqOrderNo;

	@ApiModelProperty(value = "备注")
	@TableField(value = P_REQ_REMARKS)
	private String reqRemarks;

	@ApiModelProperty(value = "自定义返回请求系统参数")
	@TableField(value = P_REQ_PARAM)
	private String reqParam;

	@ApiModelProperty(value = "支付状态 1.成功， 2.失败，3.等待")
	@TableField(value = P_STATUSS_PAY)
	private Integer statussPay;

	@ApiModelProperty(value = "请求系统key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统名称")
	@TableField(value = P_SYS_NAME)
	private String sysName;

	@ApiModelProperty(value = "用户ID")
	@TableField(value = P_USER_ID)
	private Long userId;

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

	@ApiModelProperty(value = "类型： 1. 银行转账 2. 云闪付转账 3.1 微信转账 3.2 支付宝转账 4.1 微信支付 4.2 支付宝支付 5 代理充值")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "支付平台ID")
	@TableField(value = P_PLATFORM_PAY_ID)
	private Long platformPayId;

	@ApiModelProperty(value = "支付平台key")
	@TableField(value = P_PLATFORM_PAY_KEY)
	private String platformPayKey;

	@ApiModelProperty(value = "支付平台名称")
	@TableField(value = P_PLATFORM_PAY_NAME)
	private String platformPayName;

}
