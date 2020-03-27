package org.chwin.firefighting.apiserver.net;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

    public static void setAccessControlAllowOrigin(HttpServletResponse res) {

        try{
            res.setHeader("Access-Control-Allow-Origin", "*");
        }catch (Exception e){

        }
    }
}
