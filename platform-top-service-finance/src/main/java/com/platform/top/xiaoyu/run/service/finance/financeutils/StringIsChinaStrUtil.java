package com.platform.top.xiaoyu.run.service.finance.financeutils;

/**
 * 判断字符串中有中文输入
 */
public class StringIsChinaStrUtil {

    public static boolean isChinese(char c) {
        // 根据字节码判断
        return c >= 0x4E00 &&  c <= 0x9FA5;
    }

    /**
     * 判断一个字符串是否含有中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                // 有一个中文字符就返回
                return true;
            }
        }
        return false;
    }

}
