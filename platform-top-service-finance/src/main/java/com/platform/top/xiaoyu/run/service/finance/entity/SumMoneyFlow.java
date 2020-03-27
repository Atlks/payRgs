package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 金额统计流水表
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.SUMMONEYFLOW_TABLENAME)
@ApiModel(value = "SumMoneyFlow", description = "SumMoneyFlow")
public class SumMoneyFlow extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

    /** 类型 1 充值：人工，会员 2 提现：人工，会员 3  优惠  4  洗码/返水   */
	public static final String P_TYPE = "type";
	/** 当前批次计总金额 */
	public static final String P_SUM_AMOUNT = "sum_amount";
	/** 计总最新时间 */
	public static final String P_LAST_DATETIME = "last_datetime";
	/** 开始时间 */
	public static final String P_BEGIN_DATETIME = "begin_datetime";
	/** 结束时间 */
	public static final String P_END_DATETIME = "end_datetime";

	@ApiModelProperty(value = "类型 1 充值：人工，会员 2 提现：人工，会员 3  优惠  4  洗码/返水  ")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "当前批次计总金额")
	@TableField(value = P_SUM_AMOUNT)
	private String sumAmount;

	@ApiModelProperty(value = "计总最新时间")
	@TableField(value = P_LAST_DATETIME)
	private LocalDateTime lastDatetime;

	@ApiModelProperty(value = "用户id")
	@TableField(value = P_USER_ID)
	private Long userId;

	@ApiModelProperty(value = "开始时间")
	@TableField(value = P_BEGIN_DATETIME)
	private LocalDateTime beginDatetime;

	@ApiModelProperty(value = "结束时间")
	@TableField(value = P_END_DATETIME)
	private LocalDateTime endDatetime;

}
