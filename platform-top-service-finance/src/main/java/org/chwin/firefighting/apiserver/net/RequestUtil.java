package org.chwin.firefighting.apiserver.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 





import com.google.common.collect.Maps;


/**
 * 请求封装
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class RequestUtil {
	protected final Log logger = LogFactory.getLog(getClass());
	public static int start=0;
	public static Integer pageNo=1;
	public static int pageSize=20;
	
	
	public static void main(String[] args) {
		System.out.println("--f");
	}


	
	/**
	 * 获取基础路劲
	 * @param req
	 * @return
	 */
	public static String getBasePath(HttpServletRequest req) {
		String path = req.getContextPath();
		String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;
		return basePath;
	}
		/**
	 * 获取session
	 * 
	 * @param request
	 * @return
	 */
	public static HttpSession getCRSession(HttpServletRequest request) {
		return request.getSession();
	}


	
	public static Long getLong(String name, HttpServletRequest req) {
		return Long.parseLong(req.getParameter(name));
	}

	public static Integer getInt(String name, HttpServletRequest req) {
		return Integer.parseInt(req.getParameter(name));
	}

	public static Map getMap(HttpServletRequest req) {
		Map params = new HashMap();
		Map p = req.getParameterMap();
		Iterator itor = p.keySet().iterator();
		while (itor.hasNext()) {
			String key = (String) itor.next();
			String[] values = req.getParameterValues(key);
			if (values != null && values.length > 0) {
				if (values.length > 1) {
					params.put(key, values);
				} else {
					params.put(key, values[0]);
				}
			} else {
				params.put(key, "");
			}

		}
		return params;
	}
	
	/**
	 * 将请求参数封装为Dto
	 * 
	 * @param request
	 * @return
	 */

	/**
	 * 获取参数
	 * @param url
	 * @return
	 */
//	public static String getParamFromURl(String url,String surl) {
//		String param=null;
//		if(StringUtil.isNotEmpty(url)){
//			String tempUrl = url.substring(url.lastIndexOf(surl), url.length());
//			String params[] = tempUrl.split("\\/");
//			if(params.length==3){
//			   param = params[2];
//			}
//		}
//		StringUtil.xprint("param="+param);
//		return param;
//	}
	
	//ATI q224
public	static	ThreadLocal<HttpServletResponse> resp=new ThreadLocal();

//{
//	if(params.get()==null)
//		params.set(Maps.newLinkedHashMap());
//}

	/**
	 * 直接输出.
	 * 
	 * @param contentType
	 *            内容的类型.html,text,xml的值见后，json为"text/x-json;charset=UTF-8"
	 */

//	/**
//	 * 直接输出纯字符串.
//	 */
//	public static void renderText(String text) {
//		render(text, "text/plain");
//	}
//
//	/**
//	 * 直接输出纯HTML.
//	 */
//	public static void renderHtml(String text) {
//		render(text, "text/html");
//	}

	/**
	 * 直接输出纯XML.
	 */
	public static void renderXML(String text) {
		render(text, "text/xml");
	}

	private static void render(String text, String s) {
	}
}
