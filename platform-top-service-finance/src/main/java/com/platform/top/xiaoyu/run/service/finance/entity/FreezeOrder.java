package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 冻结金额记录
 * @author coffey
 */
@Data
@TableName(TableNameConstant.FREEZE_TABLENAME)
@ApiModel(value = "FreezeOrder", description = "FreezeOrder")
public class FreezeOrder extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/**  交易记录ID */
	public static final String P_BUS_ID = "bus_id";
	/**  冻结金额 */
	public static final String P_FREEZE_MONEY = "freeze_money";
	/**  第三方游戏平台名称 */
	public static final String P_GAME_NAME = "game_name";
	/**  第三方游戏平台 */
	public static final String P_GAME_TYPE = "game_type";
	/**  批次号 */
	public static final String P_BATCH_NO = "batch_no";
	/**  冻结开始时间 */
	public static final String P_FREEZE_TIMESTARMP = "freeze_timestarmp";
	/**  类型:1 提现冻结 */
	public static final String P_TYPE = "type";
	/**  备注 */
	public static final String P_REMARKS = "remarks";
	/**  核销金额 */
	public static final String P_VERIFY_MONEY = "verify_money";

	@ApiModelProperty(value = "核销金额")
	@TableField(value = P_VERIFY_MONEY)
	private String verifyMoney;

	@ApiModelProperty(value = "交易记录ID")
	@TableField(value = P_BUS_ID)
	private Long busId;

	@ApiModelProperty(value = "冻结金额")
	@TableField(value = P_FREEZE_MONEY)
	private String freezeMoney;

	@ApiModelProperty(value = "第三方游戏平台名称")
	@TableField(value = P_GAME_NAME)
	private String gameName;

	@ApiModelProperty(value = "第三方游戏平台")
	@TableField(value = P_GAME_TYPE)
	private String gameType;

	@ApiModelProperty(value = "批次号")
	@TableField(value = P_BATCH_NO)
	private String batchNo;

	@ApiModelProperty(value = "冻结开始时间")
	@TableField(value = P_FREEZE_TIMESTARMP)
	private LocalDateTime freezeTimestarmp;

	@ApiModelProperty(value = "类型:1 提现冻结 ")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "备注")
	@TableField(value = P_REMARKS)
	private String remarks;

}
