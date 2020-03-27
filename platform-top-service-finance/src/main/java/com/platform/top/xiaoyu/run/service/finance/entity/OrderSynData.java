package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打码计算, 原数据表
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.ORDERSYN_DATA_TABLENAME)
@ApiModel(value = "OrderSynData", description = "OrderSynData")
public class OrderSynData extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 订单号 */
	public static final String P_ORDER_NO = "order_no";
	/** 游戏ID */
	public static final String P_GAME_ID = "game_id";
	/** 游戏名称 */
	public static final String P_GAME_NAME = "game_name";
	/** 投注金额 */
	public static final String P_MONEY = "money";
	/** 投注人ID */
	public static final String P_ORDER_BY_USER_ID = "order_by_user_id";
	/** 投注人账号 */
	public static final String P_ORDER_BY_NAME = "order_by_name";
	/** 投注开始时间 */
	public static final String P_ORDER_REG_DATETIME = "order_reg_datetime";
	/** 投注结束时间 */
	public static final String P_ORDER_END_DATETIME = "order_end_datetime";
	/** 批次编码 */
	public static final String P_BATCH_CODE = "batch_code";
	/** 金额参数倍数 */
	public static final String P_PARAM_CALC = "param_calc";

	@ApiModelProperty(value = "批次编码")
	@TableField(value = P_BATCH_CODE)
	private Long batchCode;

	@ApiModelProperty(value = "订单号")
	@TableField(value = P_ORDER_NO)
	private String orderNo;

	@ApiModelProperty(value = "游戏ID")
	@TableField(value = P_GAME_ID)
	private Long gameId;

	@ApiModelProperty(value = "游戏名称")
	@TableField(value = P_GAME_NAME)
	private String gameName;

	@ApiModelProperty(value = "投注金额")
	@TableField(value = P_MONEY)
	private String money;

	@ApiModelProperty(value = "投注人ID")
	@TableField(value = P_ORDER_BY_USER_ID)
	private Long orderByUserId;

	@ApiModelProperty(value = "投注人账号")
	@TableField(value = P_ORDER_BY_NAME)
	private String orderByName;

	@ApiModelProperty(value = "投注开始时间")
	@TableField(value = P_ORDER_REG_DATETIME)
	private LocalDateTime orderRegDatetime;

	@ApiModelProperty(value = "投注结束时间")
	@TableField(value = P_ORDER_END_DATETIME)
	private LocalDateTime orderEndDatetime;

	@ApiModelProperty(value = "计算参数")
	@TableField(value = P_PARAM_CALC)
	private String paramCalc;


}
