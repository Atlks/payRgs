package com.platform.top.xiaoyu.run.service.pay.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.pay.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户支付积分等级配置 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.TOOLSPAY_TABLENAME)
@ApiModel(value = "PayUserLvl", description = "用户支付积分等级配置")
public class PayUserLvl extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

	/** ip地址 */
	public static final String P_IP = "ip";
	/** 端口 */
	public static final String P_PORT = "port";

	@ApiModelProperty(value = "ip地址")
	@TableField(value = P_IP)
	private String ip;

	@ApiModelProperty(value = "端口")
	@TableField(value = P_PORT)
	private Integer port;

}
