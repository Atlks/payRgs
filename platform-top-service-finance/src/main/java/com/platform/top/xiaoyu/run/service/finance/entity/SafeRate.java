package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 保险箱利率配置
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.SAFERATE_TABLENAME)
@ApiModel(value = "SafeRate", description = "SafeRate")
public class SafeRate extends BaseEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;
	/** 利率 */
	public static final String P_RATE = "rate";
	/** 连续天数 */
	public static final String P_DAY_NUM = "day_num";

	@ApiModelProperty(value = "利率")
	@TableField(value = P_RATE)
	private BigDecimal rate;

	@ApiModelProperty(value = "连续天数")
	@TableField(value = P_DAY_NUM)
	private Integer dayNum;

}
