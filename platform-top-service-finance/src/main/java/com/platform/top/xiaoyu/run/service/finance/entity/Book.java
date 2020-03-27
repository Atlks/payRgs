package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的账本实体类
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.BOOK_TABLENAME)
@ApiModel(value = "Book", description = "Book")
public class Book extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

    /** 可用余额 */
    public static final String P_BALANCE = "balance";
	/** 上分登入游戏冻结金额 */
	public static final String P_BALANCE_NUMBER = "balance_number";
	/** 保险箱可用余额 */
	public static final String P_BALANCE_SAFE = "balance_safe";
	/** 提现冻结金额 */
	public static final String P_MONEY_EXTRACT = "money_extract";
	/** 用户name */
	public static final String P_USER_NAME = "user_name";
	/** 支付提现密码 */
	public static final String P_EXTRACT_PWD = "extract_pwd";

	@ApiModelProperty(value = "支付提现密码")
	@TableField(value = P_EXTRACT_PWD)
	private String extractPwd;

    @ApiModelProperty(value = "余额")
    @TableField(value = P_BALANCE)
    private String balance;

    @ApiModelProperty(value = "登入游戏 冻结金额")
    @TableField(value = P_BALANCE_NUMBER)
    private String balanceNumber;

	@ApiModelProperty(value = "提现冻结金额")
	@TableField(value = P_MONEY_EXTRACT)
	private String moneyExtract;

    @ApiModelProperty(value = "保险箱可用余额")
    @TableField(value = P_BALANCE_SAFE)
    private String balanceSafe;

	@ApiModelProperty(value = "用户NAME")
	@TableField(value = P_USER_NAME)
	private String userName;


}
