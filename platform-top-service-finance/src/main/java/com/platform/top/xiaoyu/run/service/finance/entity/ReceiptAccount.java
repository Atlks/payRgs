package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收款账号
 */
@Data
@TableName(TableNameConstant.ACCOUNT_TABLENAME)
@ApiModel(value = "ReceiptAccount", description = "ReceiptAccount")
public class ReceiptAccount extends BaseEntity<Long> {

	/**  卡号  */
	public static final String P_ACCOUNT_NO = "account_no";
	/**  姓名  */
	public static final String P_ACCOUNT_NAME = "account_name";
	/**  所属银行  */
	public static final String P_BANK_NAME = "bank_name";
	/**  所属银行code  */
	public static final String P_BANK_CODE = "bank_code";
	/**  配置 表ID  */
	public static final String P_TOOLS_ID = "tools_id";
	/**  图标url  */
	public static final String P_ICON_URL = "icon_url";
	/**  二维码url  */
	public static final String P_QR_CODE_URL = "qr_code_url";
	/**  银行地址  */
	public static final String P_BANK_ADDRESS = "bank_address";
	/**  账号类型 1 QQ 2 支付宝 3 微信 */
	public static final String P_ACCOUNT_TYPE = "account_type";
	/**  账号类型str  */
	public static final String P_ACCOUNT_TYPE_STR = "account_type_str";
	/**  标题  */
	public static final String P_TITLE = "title";
	/** 排序 */
	public static final String P_SROT_BY = "srot_by";

	@ApiModelProperty(value = "卡号")
	@TableField(value = P_ACCOUNT_NO)
	private String accountNo;

	@ApiModelProperty(value = "姓名")
	@TableField(value = P_ACCOUNT_NAME)
	private String accountName;

	@ApiModelProperty(value = "所属银行")
	@TableField(value = P_BANK_NAME)
	private String bankName;

	@ApiModelProperty(value = "配置表ID")
	@TableField(value = P_TOOLS_ID)
	private Long toolsId;

	@ApiModelProperty(value = "所属银行code")
	@TableField(value = P_BANK_CODE)
	private String bankCode;

	@ApiModelProperty(value = "图标url")
	@TableField(value = P_ICON_URL)
	private String iconUrl;

	@ApiModelProperty(value = "二维码url")
	@TableField(value = P_QR_CODE_URL)
	private String qrCodeUrl;

	@ApiModelProperty(value = "银行地址")
	@TableField(value = P_BANK_ADDRESS)
	private String bankAddress;

	@ApiModelProperty(value = "账号类型 1 QQ 2 支付宝 3 微信")
	@TableField(value = P_ACCOUNT_TYPE)
	private Integer accountType;

	@ApiModelProperty(value = "账号类型str")
	@TableField(value = P_ACCOUNT_TYPE_STR)
	private String accountTypeStr;

	@ApiModelProperty(value = "标题")
	@TableField(value = P_TITLE)
	private String title;

	@ApiModelProperty(value = "排序")
	@TableField(value = P_SROT_BY)
	private Integer srotBy;

}
