package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 金额统计表
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.SUMMONEY_TABLENAME)
@ApiModel(value = "SumMoney", description = "SumMoney")
public class SumMoney extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

    /** 类型 1 充值：人工，会员 2 提现：人工，会员 3  优惠  4  洗码/返水  */
	public static final String P_TYPE = "type";
	/** 当前批次计总金额 */
	public static final String P_SUM_AMOUNT = "sum_amount";
	/** 计总最新时间 */
	public static final String P_LAST_DATETIME = "last_datetime";
	/** 计总次数 */
	public static final String P_SUM_NUMBER = "sum_number";

	@ApiModelProperty(value = "类型 1 充值：人工，会员 2 提现：人工，会员 3  优惠  4  洗码/返水 ")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "当前批次计总金额")
	@TableField(value = P_SUM_AMOUNT)
	private String sumAmount;

	@ApiModelProperty(value = "计总最新时间")
	@TableField(value = P_LAST_DATETIME)
	private LocalDateTime lastDatetime;

	@ApiModelProperty(value = "计总次数")
	@TableField(value = P_SUM_NUMBER)
	private Integer sumNumber;

}
