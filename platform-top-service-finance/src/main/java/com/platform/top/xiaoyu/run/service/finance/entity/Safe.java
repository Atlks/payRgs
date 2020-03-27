package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.finance.constant.TableNameConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险箱记录
 *
 * @author coffey
 */
@Data
@TableName(TableNameConstant.SAFEINFO_TABLENAME)
@ApiModel(value = "Safe", description = "保险箱记录")
public class Safe extends BaseUserIdEntity<Long> {

    private static final long serialVersionUID = -3118619793714361990L;

    /** 保险箱密码 */
	public static final String P_PWD = "pwd";

	@ApiModelProperty(value = "保险箱密码")
	@TableField(value = P_PWD)
	private String pwd;

}
