package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息金额变动
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.MSGBUS_TABLENAME)
@ApiModel(value = "MsgBus", description = "MsgBus")
public class MsgBus extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 消息code */
	public static final String P_CODE = "code";
	/** 金额 */
	public static final String P_AMOUNT = "amount";
	/** 消息内容 */
	public static final String P_MSG = "msg";
	/** 消息类型 */
	public static final String P_TYPE = "type";
	/** 消息类型str */
	public static final String P_TYPE_STR = "type_str";

    @ApiModelProperty(value = "消息code")
    @TableField(value = P_CODE)
    private String code;

	@ApiModelProperty(value = "金额")
	@TableField(value = P_AMOUNT)
	private String amount;

	@ApiModelProperty(value = "消息内容")
	@TableField(value = P_MSG)
	private String msg;

	@ApiModelProperty(value = "消息类型 交易类型")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "消息类型str")
	@TableField(value = P_TYPE_STR)
	private String typeStr;


}
