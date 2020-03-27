package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 绑卡记录 实体类
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.CARD_TABLENAME)
@ApiModel(value = "BankBinding", description = "BankBinding")
public class BankBinding extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 类型：银行卡，支付宝，微信，PayPal 等 */
	public static final String P_TYPE = "type";
	/** 所属银行code */
	public static final String P_BANK_CODE = "bank_code";
	/** 所属银行 */
	public static final String P_BANK_NAME = "bank_name";
	/** 所属银行地址 */
	public static final String P_BANK_ADDRESS = "bank_address";
	/** 银行卡号/第三方账号 */
	public static final String P_ACCOUNT_NO = "account_no";
	/** 银行卡姓名 */
	public static final String P_ACCOUNT_NAME = "account_name";
	/** 创建人 */
	public static final String P_CREATE_BY = "create_by";
	/** 创建人id */
	public static final String P_CREATE_USER_ID = "create_user_id";
	/**  背景图  */
	public static final String P_URL_BACKROUG = "url_backroug";
	/**  图标  */
	public static final String P_URL_ICON = "url_icon";
	/**  用户Name  */
	public static final String P_USER_NAME = "user_name";

	@ApiModelProperty(value = "用户Name")
	@TableField(value = P_USER_NAME)
	private String userName;

    @ApiModelProperty(value = "类型：银行卡，支付宝，微信，PayPal 等")
    @TableField(value = P_TYPE)
    private Integer type;

    @ApiModelProperty(value = "所属银行code")
    @TableField(value = P_BANK_CODE)
    private String bankCode;

	@ApiModelProperty(value = "所属银行")
	@TableField(value = P_BANK_NAME)
	private String bankName;

	@ApiModelProperty(value = "所属银行地址")
	@TableField(value = P_BANK_ADDRESS)
	private String bankAddress;

    @ApiModelProperty(value = "银行卡号/第三方账号")
    @TableField(value = P_ACCOUNT_NO)
    private String accountNo;

    @ApiModelProperty(value = "银行卡姓名")
    @TableField(value = P_ACCOUNT_NAME)
    private String accountName;

	@ApiModelProperty(value = "创建人")
	@TableField(value = P_CREATE_BY)
	private String createBy;

	@ApiModelProperty(value = "创建人id")
	@TableField(value = P_CREATE_USER_ID)
	private Long createUserId;

	@ApiModelProperty(value = "背景图")
	@TableField(value = P_URL_BACKROUG)
	private String urlBackroug;

	@ApiModelProperty(value = "图标")
	@TableField(value = P_URL_ICON)
	private String urlIcon;

}
