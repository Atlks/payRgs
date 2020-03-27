package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险箱流水记录
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.SAFEFLOW_TABLENAME)
@ApiModel(value = "SafeFlow", description = "SafeFlow")
public class SafeFlow extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/**  金额  */
	public static final String P_AMOUNT = "amount";
	/**  类型( 1 转入、2 转出 )  */
	public static final String P_TYPE = "type";
	/**  备注  */
	public static final String P_REMARKS = "remarks";
	/**  可用余额  */
	public static final String P_BALANCE = "balance";
	/**  保险箱余额  */
	public static final String P_BALANCE_SAFE = "balance_safe";

	@ApiModelProperty(value = "金额")
	@TableField(value = P_AMOUNT)
	private String amount;

	@ApiModelProperty(value = "类型( 1 转入、2 转出 )")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "备注")
	@TableField(value = P_REMARKS)
	private String remarks;

	@ApiModelProperty(value = "可用余额")
	@TableField(value = P_BALANCE)
	private String balance;

	@ApiModelProperty(value = "保险箱余额")
	@TableField(value = P_BALANCE_SAFE)
	private String balanceSafe;

}
