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
 * 白名单 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.WHITELIST_TABLENAME)
@ApiModel(value = "Whitelist", description = "白名单")
public class Whitelist extends BaseEntity<Long>  {

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
