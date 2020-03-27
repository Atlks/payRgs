package com.platform.top.xiaoyu.run.service.pay.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.top.xiaoyu.run.service.pay.constant.TableNameConstant;
import com.top.xiaoyu.rearend.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付平台token 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.TOKEN_TABLENAME)
@ApiModel(value = "TokenPay", description = "支付平台token")
public class TokenPay extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

	/**  token码  */
	public static final String P_TOKEN = "token";
	/**  系统密钥  */
	public static final String P_SYS_KEY = "sys_key";
	/**  第三方系统名称  */
	public static final String P_SYS_NAME = "sys_name";
	/**  有效到期时间  */
	public static final String P_END_DATETIME = "end_datetime";

	@ApiModelProperty(value = "ip地址")
	@TableField(value = P_TOKEN)
	private String token;

	@ApiModelProperty(value = "请求系统Key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统名称")
	@TableField(value = P_SYS_NAME)
	private String sysName;

	@ApiModelProperty(value = "有效到期时间")
	@TableField(value = P_END_DATETIME)
	private LocalDateTime endDatetime;

}
