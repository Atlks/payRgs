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
 * 第三方支付平台 实体类
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(TableNameConstant.PLATFORM_TABLENAME)
@ApiModel(value = "PayPlatform", description = "第三方支付平台记录")
public class PayPlatform extends BaseEntity<Long> {

    /** 商户ID */
    public static final String P_APP_ID = "app_id";
    /** 商户密钥 */
    public static final String P_APP_KEY = "app_key";
    /** 商户名称 */
    public static final String P_APP_NAME = "app_name";
    /** 商户URL */
    public static final String P_APP_URL = "app_url";
    /** 有效到期时间 */
    public static final String P_END_DATETIME = "end_datetime";
    /** 支付类型图标 */
    public static final String P_ICON_URL = "icon_url";
    /** 是否推荐 1 是 2 否 */
    public static final String P_PRIORITY = "priority";
    /** 线上线下 1 线上 2  线下 */
    public static final String P_ONLINE = "online";
    /** 排序 */
    public static final String P_SORT_BY = "sort_by";
    /** 加密方式 NOT_ENCRYPT, MD5, BASE64 */
    public static final String P_ENCRYPT = "encrypt";
    /** 支付平台key： okf, mango */
    public static final String P_PAY_PLATFROM_KEY = "pay_platfrom_key";
    /** 支付类型  例： mango支付平台==》 963 银联扫码/云闪付   972 支付宝  975 支付宝转卡 扫码方式  985 微信转卡  986 zfb转卡 跳转页面 */
    public static final String P_PAY_TYPE = "pay_type";
    public static final String P_PAY_TYPE_STR = "pay_type_str";
    /** 已发布的所有平台ID */
    public static final String P_PUSH_PLATFORM_IDS = "push_platform_ids";

    @ApiModelProperty(value = "已发布的所有平台ID")
    @TableField(value = P_PUSH_PLATFORM_IDS)
    private String pushPlatformIds;

    @ApiModelProperty(value = "商户ID")
    @TableField(value = P_APP_ID)
    private String appId;

    @ApiModelProperty(value = "商户密钥")
    @TableField(value = P_APP_KEY)
    private String appKey;

    @ApiModelProperty(value = "商户名称")
    @TableField(value = P_APP_NAME)
    private String appName;

    @ApiModelProperty(value = "商户URL")
    @TableField(value = P_APP_URL)
    private String appUrl;

    @ApiModelProperty(value = "有效到期时间")
    @TableField(value = P_END_DATETIME)
    private LocalDateTime endDatetime;

    @ApiModelProperty(value = "支付类型图标")
    @TableField(value = P_ICON_URL)
    private String iconUrl;

    @ApiModelProperty(value = "是否推荐 1 是 2 否")
    @TableField(value = P_PRIORITY)
    private Integer priority;

    @ApiModelProperty(value = "线上线下 1 线上 2  线下")
    @TableField(value = P_ONLINE)
    private Integer online;

    @ApiModelProperty(value = "排序")
    @TableField(value = P_SORT_BY)
    private Integer sortBy;

    @ApiModelProperty(value = "加密方式 NOT_ENCRYPT, MD5, BASE64")
    @TableField(value = P_ENCRYPT)
    private String encrypt;

    @ApiModelProperty(value = "支付平台key： okf, mango")
    @TableField(value = P_PAY_PLATFROM_KEY)
    private String payPlatfromKey;

    @ApiModelProperty(value = "支付方式  例： OKF 支付平台 ==》 1 微信支付 2 支付宝支付 3 银联支付")
    @TableField(value = P_PAY_TYPE)
    private String payType;

    @ApiModelProperty(value = "支付方式  例： OKF 支付平台 ==》 1 微信支付 2 支付宝支付 3 银联支付")
    @TableField(value = P_PAY_TYPE_STR)
    private String payTypeStr;

}
