package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收款配置
 */
@Data
@TableName(TableNameConstant.ACTTOOLS_TABLENAME)
@ApiModel(value = "ReceiptAccountTools", description = "ReceiptAccountTools")
public class ReceiptAccountTools extends BaseEntity<Long> {

	/** 类型 1 银行卡转账 2 云闪付 3 微信转账 4 支付宝转账 5 微信支付 6 支付宝支付 7 代理充值 8 银联支付 */
	public static final String P_TYPE = "type";
	/** 名称 */
	public static final String P_NAME = "name";
	/** 图标url */
	public static final String P_ICON_URL = "icon_url";
	/** 最大值 */
	public static final String P_MAX_VAL = "max_val";
	/** 最小值 */
	public static final String P_MIN_VAL = "min_val";
	/** 优惠标识 1 启用，2 禁用 */
	public static final String P_OFFER = "offer";
	/** 优惠备注 */
	public static final String P_OFFER_STR = "offer_str";
	/** 页面排版类型 1 表示线上（银行卡转账）2 线下(2 云闪付 3 微信转账 4 支付宝转账), 3 代理(代理充值), 4 支付跳转(5 微信支付 6 支付宝支付 8 银联支付) */
	public static final String P_TYPE_PAGE = "type_page";
	/** 排序 */
	public static final String P_SROT_BY = "srot_by";
	/** 支付平台id */
	public static final String P_PAY_PLATFORM_ID = "pay_platform_id";

	@ApiModelProperty(value = "支付平台id")
	@TableField(value = P_PAY_PLATFORM_ID)
	private String payPlatformId;

	@ApiModelProperty(value = "类型 1 银行卡转账 2 云闪付 3 微信转账 4 支付宝转账 5 微信支付 6 支付宝支付 7 代理充值 8 银联支付")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "名称")
	@TableField(value = P_NAME)
	private String name;

	@ApiModelProperty(value = "图标url")
	@TableField(value = P_ICON_URL)
	private String iconUrl;

	@ApiModelProperty(value = "最大值")
	@TableField(value = P_MAX_VAL)
	private Integer maxVal;

	@ApiModelProperty(value = "最小值")
	@TableField(value = P_MIN_VAL)
	private Integer minVal;

	@ApiModelProperty(value = "优惠标识 1启用，2禁用")
	@TableField(value = P_OFFER)
	private Integer offer;

	@ApiModelProperty(value = "优惠备注")
	@TableField(value = P_OFFER_STR)
	private String offerStr;

	@ApiModelProperty(value = "页面排版类型 1 表示线上（银行卡转账）2 线下(2 云闪付 3 微信转账 4 支付宝转账), 3 代理(代理充值), 4 支付跳转(5 微信支付 6 支付宝支付 8 银联支付)")
	@TableField(value = P_TYPE_PAGE)
	private Integer typePage;

	@ApiModelProperty(value = "排序")
	@TableField(value = P_SROT_BY)
	private Integer srotBy;

}
