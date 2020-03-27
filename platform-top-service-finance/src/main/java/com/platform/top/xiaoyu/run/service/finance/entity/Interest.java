package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 利息计算表
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.INTEREST_TABLENAME)
@ApiModel(value = "Interest", description = "Interest")
public class Interest extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;
	/** 类型 1 保险箱 */
	public static final String P_TYPE = "type";
	/** 计算日期 */
	public static final String P_CALE_DATE = "cale_date";
	/** 当前批次利息金额 */
	public static final String P_MONEY = "money";
	/** 计算参数 */
	public static final String P_PARAM_CALC = "param_calc";


	@ApiModelProperty(value = "利率")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "连续天数")
	@TableField(value = P_CALE_DATE)
	private LocalDate caleDate;

	@ApiModelProperty(value = "当前计算利息金额")
	@TableField(value = P_MONEY)
	private String money;

	@ApiModelProperty(value = "计算参数")
	@TableField(value = P_PARAM_CALC)
	private String paramCalc;

}
