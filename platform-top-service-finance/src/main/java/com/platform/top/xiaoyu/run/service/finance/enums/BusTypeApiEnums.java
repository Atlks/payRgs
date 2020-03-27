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
 * 前台状态字段
 * 交易流水 交易类型 枚举值
 * @author coffey
 */
public enum BusTypeApiEnums implements EnumerableNameAndValue {

//	1 登入、2 登出、3 会员充值、4 会员提现、5 人工取款、7 领取佣金、8 红包、10 结算入账、11 领取洗码、12 赠送彩金、13 活动优惠、14 人工存入、
//  9 保险箱转出 16 保险箱转入 17 签到彩金 18 首存彩金 19 优惠 20 普及彩金 21 人工赠送彩金 22 充值优惠彩金 23 洗码 24 平台资金切换 25 VIP晋升礼金 26 注册赠送彩金

	//	1000 所有状态， 24 平台资金切换，  3 会员充值， 4 会员提现， 14 人工存入，13 活动优惠，5 人工取款， 23 洗码， 12 赠送彩金， 11 领取洗码，
	//	7 领取佣金，
	//	30 保险箱记录，10 结算存入，8 红包

	All( "所有状态", 1000),
	PLATFORM_MONEY("平台资金切换", 24),
	RECHARGE_ONLINE("会员充值", 3),
	EXTRACT_ONLINE("会员提现", 4),
	RECHARGE_OFFLINE("人工存入",14),
	ACTIVITY_OFFER("活动优惠", 13),
	EXTRACT_OFFLINE("人工取款", 5),
	ACTIVITY_LOTTERY("赠送彩金", 12),
	ACTIVITY_CODE_ADD("洗码", 23),
	ACTIVITY_CODE("领取洗码", 11),
	ACTIVITY_REBATE("领取佣金", 7),
	ACTIVITY_PACK("红包", 8),
	SYS_ORDEREND("结算存入", 10),
	SAFE_ALL("保险箱记录", 30),
	VIP_UP("VIP晋升礼金", 25),
	ACTIVITY_REGUSER("注册赠送彩金", 26),


	;

	@Getter
	private String name;
	@Getter
	private Integer val;

	BusTypeApiEnums(String name , Integer val) {
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

	public static BusTypeApiEnums getType(Integer dataTypeCode){
		for(BusTypeApiEnums enums : BusTypeApiEnums.values()){
//			if(enums.getVal().equals(dataTypeCode)){
//				return enums;
//			}
		}
		return null;
	}

}
