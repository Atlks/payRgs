package org.chwin.firefighting.apiserver.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.util.Map;

public class ExceptionUtil {

    public static void main(String[] args) {
        System.out.println( ExceptionUtil. getExceptionJson(new RuntimeException("参数错误")));
    }


    public static String getEx(Exception e) {
        Map m = Maps.newLinkedHashMap();
        m.put("e", e);
        m.put("errcode", 500);
        String s = JSON.toJSONString(m, true);
        return s;
        //          res.getWriter().write(s);
//          res.getWriter().flush();
//          res.flushBuffer();
    }

    public static String getExceptionJson(Exception e) {
        return  getEx(  e);
    }
}
