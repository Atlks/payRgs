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
 * 用户支付等级 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.USERLVL_TABLENAME)
@ApiModel(value = "UserLvl", description = "用户支付等级")
public class UserLvl extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

    /** 用户name */
	public static final String P_USER_NAME = "user_name";
	/** 用户ID */
	public static final String P_USER_ID = "user_id";
	/** 等级 */
	public static final String P_LVL = "lvl";
	/** 积分: 1 元 == 100 分 */
	public static final String P_NUMBER = "number";
	/** 请求系统key */
	public static final String P_SYS_KEY = "sys_key";
	/** 请求系统名称 */
	public static final String P_SYS_NAME = "sys_name";

	@ApiModelProperty(value = "等级")
	@TableField(value = P_LVL)
	private Integer lvl;

	@ApiModelProperty(value = "积分: 1 元 == 100 分")
	@TableField(value = P_NUMBER)
	private Integer number;

	@ApiModelProperty(value = "用户ID")
	@TableField(value = P_USER_ID)
	private Long userId;

	@ApiModelProperty(value = "用户name")
	@TableField(value = P_USER_NAME)
	private String userName;

	@ApiModelProperty(value = "请求系统key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统名称")
	@TableField(value = P_SYS_NAME)
	private String sysName;

}
