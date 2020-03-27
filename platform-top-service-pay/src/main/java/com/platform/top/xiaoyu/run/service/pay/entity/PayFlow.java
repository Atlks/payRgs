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
 * 支付流水 实体类
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.PAYFLOW_TABLENAME)
@ApiModel(value = "PayFlow", description = "支付流水")
public class PayFlow extends BaseEntity<Long>  {

    private static final long serialVersionUID = -3118619793714361990L;

	/** 请求参数 */
	public static final String P_PARAM_REQ = "param_req";
	/** 返回参数 */
	public static final String P_PARAM_RESP = "param_resp";
	/** 类型：1 主动请求，2 回调  3 接收回调请求*/
	public static final String P_TYPE = "type";
	/** 请求支付金额 */
	public static final String P_MONEY = "money";
	/** 超时时间秒数 */
	public static final String P_TIME_OUT_SECOND = "time_out_second";
	/** 超时时间 */
	public static final String P_TIME_OUT_DATE = "time_out_date";
	/** 回调URL */
	public static final String P_CALL_URL = "call_url";
	/** 回调参数 */
	public static final String P_CALL_PARAM = "call_param";
	/** 回调类型：get，post，put */
	public static final String P_CALL_TYPE = "call_type";
	/** 请求系统key */
	public static final String P_SYS_KEY = "sys_key";
	/** 请求系统名称 */
	public static final String P_SYS_NAME = "sys_name";
	/** 支付单号 */
	public static final String P_PAY_NO = "pay_no";
	/** 订单号 */
	public static final String P_ORDER_NO = "order_no";
	/** 调用支付接口状态 1.成功， 2.失败，3.等待 */
	public static final String P_STATUSS_PAY = "statuss_pay";
	/** 用户id */
	public static final String P_USER_ID = "user_id";
	/**  支付平台ID  */
	public static final String P_PLATFORM_PAY_ID = "platform_pay_id";
	/**  支付平台key  */
	public static final String P_PLATFORM_PAY_KEY = "platform_pay_key";
	/**  支付平台名称  */
	public static final String P_PLATFORM_PAY_NAME = "platform_pay_name";

	@ApiModelProperty(value = "请求参数")
	@TableField(value = P_PARAM_REQ)
	private String paramReq;

	@ApiModelProperty(value = "返回参数")
	@TableField(value = P_PARAM_RESP)
	private String paramResp;

	@ApiModelProperty(value = "类型：1主动请求，2回调")
	@TableField(value = P_TYPE)
	private Integer type;

	@ApiModelProperty(value = "请求支付金额")
	@TableField(value = P_MONEY)
	private String money;

	@ApiModelProperty(value = "超时时间秒数")
	@TableField(value = P_TIME_OUT_SECOND)
	private Integer timeOutSecond;

	@ApiModelProperty(value = "最后超时时间")
	@TableField(value = P_TIME_OUT_DATE)
	private Integer timeOutDate;

	@ApiModelProperty(value = "回调参数")
	@TableField(value = P_CALL_URL)
	private String callUrl;

	@ApiModelProperty(value = "回调参数")
	@TableField(value = P_CALL_PARAM)
	private String callParam;

	@ApiModelProperty(value = "回调类型：get，post，put")
	@TableField(value = P_CALL_TYPE)
	private Integer callType;

	@ApiModelProperty(value = "请求系统key")
	@TableField(value = P_SYS_KEY)
	private String sysKey;

	@ApiModelProperty(value = "请求系统name")
	@TableField(value = P_SYS_NAME)
	private String sysName;

	@ApiModelProperty(value = "支付单号")
	@TableField(value = P_PAY_NO)
	private Long payNo;

	@ApiModelProperty(value = "订单号")
	@TableField(value = P_ORDER_NO)
	private String orderNo;

	@ApiModelProperty(value = "调用支付接口状态")
	@TableField(value = P_STATUSS_PAY)
	private Integer statussPay;

	@ApiModelProperty(value = "用户id")
	@TableField(value = P_USER_ID)
	private Long userId;

	@ApiModelProperty(value = "支付平台ID")
	@TableField(value = P_PLATFORM_PAY_ID)
	private Long platformPayId;

	@ApiModelProperty(value = "支付平台key")
	@TableField(value = P_PLATFORM_PAY_KEY)
	private String platformPayKey;

	@ApiModelProperty(value = "支付平台名称")
	@TableField(value = P_PLATFORM_PAY_NAME)
	private String platformPayName;

}
