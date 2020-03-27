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
 * 用户支付积分等级配置表 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.TOOLSPAY_TABLENAME)
@ApiModel(value = "ToolsPay", description = "用户支付积分等级配置表")
public class ToolsPay extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

	/**  等级  */
	public static final String P_LVL = "lvl";
	/**  积分: 1 元 == 100 分  */
	public static final String P_NUMBER = "number";
	/**  请求系统key  */
	public static final String P_SYS_KEY = "sys_key";
	/**  请求系统名称  */
	public static final String P_SYS_NAME = "sys_name";
	/**  优先级（填写key优先值为1）  */
	public static final String P_PRIORITY = "priority";

	@ApiModelProperty(value = "ip地址")
	@TableField(value = P_LVL)
	private Integer lvl;

	@ApiModelProperty(value = "分值")
	@TableField(value = P_NUMBER)
	private Integer number;

	@ApiModelProperty(value = "请求系统key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统名称")
	@TableField(value = P_SYS_NAME)
	private String sysName;

	@ApiModelProperty(value = "优先级")
	@TableField(value = P_PRIORITY)
	private Integer priority;

}
