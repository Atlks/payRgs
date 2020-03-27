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
 * 利息计算类型 枚举值
 * @author coffey
 */
public enum InterestTypeEnums implements EnumerableNameAndValue {

	SAFE("保险箱", 1),

	;

	@Getter
	private String name;
	@Getter
	private Integer val;

	InterestTypeEnums(String name , Integer val) {
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

	public static InterestTypeEnums getType(Integer dataTypeCode){
		for(InterestTypeEnums enums : InterestTypeEnums.values()){
//			if(enums.getVal().equals(dataTypeCode)){
//				return enums;
//			}
		}
		return null;
	}

}
