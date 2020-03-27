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
 * 接口配置表超时积分等级 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.TIMEOUT_TABLENAME)
@ApiModel(value = "TimeOut", description = "接口配置表超时积分等级")
public class TimeOut extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

    /**  超时秒数  */
	public static final String P_TIME_OUT_SECOND = "time_out_second";
	/**  请求系统key  */
	public static final String P_SYS_KEY = "sys_key";
	/**  请求系统名称  */
	public static final String P_SYS_NAME = "sys_name";
	/**  等级  */
	public static final String P_LVL = "lvl";
	/**  分值  */
	public static final String P_NUMBER = "number";

	@ApiModelProperty(value = "超时秒数")
	@TableField(value = P_TIME_OUT_SECOND)
	private Integer timeOutSecond;

	@ApiModelProperty(value = "请求系统Key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统名称")
	@TableField(value = P_SYS_NAME)
	private String sysName;

	@ApiModelProperty(value = "等级")
	@TableField(value = P_LVL)
	private String lvl;

	@ApiModelProperty(value = "分值")
	@TableField(value = P_NUMBER)
	private String number;

}
