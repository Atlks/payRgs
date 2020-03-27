package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收款值配置
 */
@Data
@TableName(TableNameConstant.ACTVALUE_TABLENAME)
@ApiModel(value = "ReceiptAccountValue", description = "ReceiptAccountValue")
public class ReceiptAccountValue extends BaseEntity<Long> {

	/** 配置表id */
	public static final String P_TOOLS_ID = "tools_id";
	/** 值 */
	public static final String P_VALUE = "value";

	@ApiModelProperty(value = "配置表id")
	@TableField(value = P_TOOLS_ID)
	private Long toolsId;

	@ApiModelProperty(value = "值")
	@TableField(value = P_VALUE)
	private Integer value;

}
