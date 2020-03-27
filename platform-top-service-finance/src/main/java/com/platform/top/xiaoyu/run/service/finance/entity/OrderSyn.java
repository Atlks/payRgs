package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打码计算
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.ORDERSYN_TABLENAME)
@ApiModel(value = "OrderSyn", description = "OrderSyn")
public class OrderSyn extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 交易ID */
	public static final String P_BUS_ID = "bus_id";
	/** 交易CODE */
	public static final String P_BUS_CODE = "bus_code";
	/** 最后订单同步时间 */
	public static final String P_LAST_ORDER_DATETIME = "last_order_datetime";
	/** 批次编码 */
	public static final String P_BATCH_CODE = "batch_code";
	/** 最后一次打码剩余金额 */
	public static final String P_LAST_AMOUNT = "last_amount";
	/** 打码金额 */
	public static final String P_COUNT_AMOUNT = "count_amount";

	@ApiModelProperty(value = "打码金额")
	@TableField(value = P_COUNT_AMOUNT)
	private String countAmount;

	@ApiModelProperty(value = "交易ID")
	@TableField(value = P_BUS_ID)
	private Long busId;

	@ApiModelProperty(value = "交易CODE")
	@TableField(value = P_BUS_CODE)
	private String busCode;

	@ApiModelProperty(value = "最后订单同步时间")
	@TableField(value = P_LAST_ORDER_DATETIME)
	private LocalDateTime lastOrderDatetime;

	@ApiModelProperty(value = "批次编码")
	@TableField(value = P_BATCH_CODE)
	private Long batchCode;

	@ApiModelProperty(value = "最后一次打码剩余金额")
	@TableField(value = P_LAST_AMOUNT)
	private String lastAmount;


}
