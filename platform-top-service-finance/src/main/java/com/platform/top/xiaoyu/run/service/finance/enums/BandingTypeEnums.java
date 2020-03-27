package com.platform.top.xiaoyu.run.service.finance.enums;

import com.platform.top.xiaoyu.run.service.api.common.converter.BaseEnumValueConverter;

import com.platform.top.xiaoyu.run.service.api.common.enums.EnumerableNameAndValue;
import com.platform.top.xiaoyu.run.service.api.common.enums.PlatformDrawMode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绑卡类型 枚举值
 * @author coffey
 */
public enum BandingTypeEnums implements EnumerableNameAndValue {

// 类型：1 银行卡，2 支付宝，3 微信，4 PayPal

	BANK( "银行卡", 1),
	WEIXIN( "微信", 2),
	ZFB( "支付宝", 3),
	SFT( "闪付通", 4),
	PAYPAL( "闪付通", 5),

	;

	@Getter
	private String name;
	@Getter
	private Integer val;

	BandingTypeEnums(String name , Integer val) {
		this.name = name;
		this.val = val;
	}


	public static class Converter extends BaseEnumValueConverter<PlatformDrawMode> {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getValue() {
		return val;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getList(){
		List list = new ArrayList();
		for (PlatformDrawMode enumVal : PlatformDrawMode.values()) {
			Map<String, String> map = new ConcurrentHashMap<>();
			map.put("value", String.valueOf(enumVal.getValue()));
			map.put("name", enumVal.getName());
			list.add(map);
		}
		return list;
	}

	public static BandingTypeEnums getType(Integer dataTypeCode){
		for(BandingTypeEnums enums : BandingTypeEnums.values()){
//			if(enums.getVal().equals(dataTypeCode)){
//				return enums;
//			}
		}
		return null;
	}

}
