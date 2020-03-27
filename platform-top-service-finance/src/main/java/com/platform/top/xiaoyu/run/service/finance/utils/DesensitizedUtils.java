package com.platform.top.xiaoyu.run.service.finance.utils;

import com.platform.top.xiaoyu.run.service.api.user.annotations.Desensitized;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @Authror tienan
 * @Date 2019/8/26 18:30
 */
public class DesensitizedUtils {


	public static  Object  desensitization(Object obj){
		try {
			return ObjectDecrypt(obj);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private static Object ObjectDecrypt(Object obj) throws Exception {
		Class<? extends Object> cls = obj.getClass();
		for (Field field : cls.getDeclaredFields()) {
			field.setAccessible(true);
            //得到参数
			Object value = field.get(obj);
			if (value instanceof String) {
				Desensitized annotation = field.getAnnotation(Desensitized.class);
				String valueStr = (String) value;
				if (StringUtils.isNotBlank(valueStr) && annotation != null) {
					switch (annotation.type()) {
						case CHINESE_NAME: {
							field.set(obj, DesensitizedUtils.chineseName(valueStr));
							break;
						}
						case ID_CARD: {
							field.set(obj, DesensitizedUtils.idCardNum(valueStr));
							break;
						}
						case FIXED_PHONE: {
							field.set(obj, DesensitizedUtils.fixedPhone(valueStr));
							break;
						}
						case MOBILE_PHONE: {
							field.set(obj, DesensitizedUtils.mobilePhone(valueStr));
							break;
						}
						case ADDRESS: {
							field.set(obj, DesensitizedUtils.address(valueStr, 8));
							break;
						}
						case EMAIL: {
							field.set(obj, DesensitizedUtils.email(valueStr));
							break;
						}
						case BANK_CARD: {
							field.set(obj, DesensitizedUtils.bankCard(valueStr));
							break;
						}
						case PASSWORD: {
							field.set(obj, DesensitizedUtils.password(valueStr));
							break;
						}
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
	 *
	 * @param fullName
	 * @return
	 */
	public static String chineseName(String fullName) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		}
		String name = StringUtils.left(fullName, 1);
		return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
	}

	/**
	 * 【身份证号】显示最后四位，其他隐藏。共计18位或者15位，比如：*************1234
	 *
	 * @param id
	 * @return
	 */
	public static String idCardNum(String id) {
		if (StringUtils.isBlank(id)) {
			return "";
		}
		String num = StringUtils.right(id, 4);
		return StringUtils.leftPad(num, StringUtils.length(id), "*");
	}

	/**
	 * 【固定电话 后四位，其他隐藏，比如1234
	 *
	 * @param num
	 * @return
	 */
	public static String fixedPhone(String num) {
		if (StringUtils.isBlank(num)) {
			return "";
		}
		return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
	}

	/**
	 * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
	 *
	 * @param num
	 * @return
	 */
	public static String mobilePhone(String num) {
		if (StringUtils.isBlank(num)) {
			return "";
		}
		return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
	}

	/**
	 * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	 *
	 * @param address
	 * @param sensitiveSize 敏感信息长度
	 * @return
	 */
	public static String address(String address, int sensitiveSize) {
		if (StringUtils.isBlank(address)) {
			return "";
		}
		int length = StringUtils.length(address);
		return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
	}

	/**
	 * 【电子邮箱 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com>
	 *
	 * @param email
	 * @return
	 */
	public static String email(String email) {
		if (StringUtils.isBlank(email)) {
			return "";
		}
		int index = StringUtils.indexOf(email, "@");
		if (index <= 1)
			return email;
		else
			return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
	}

	/**
	 * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234>
	 *
	 * @param cardNum
	 * @return
	 */
	public static String bankCard(String cardNum) {
		if (StringUtils.isBlank(cardNum)) {
			return "";
		}
		return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 *
	 * @param password
	 * @return
	 */
	public static String password(String password) {
		if (StringUtils.isBlank(password)) {
			return "";
		}
		String pwd = StringUtils.left(password, 0);
		return StringUtils.rightPad(pwd, StringUtils.length(password), "*");
	}


}
