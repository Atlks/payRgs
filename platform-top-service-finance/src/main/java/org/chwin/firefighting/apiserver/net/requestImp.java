/**
 * @author attilax 老哇的爪子
	@since  2014-4-28 上午11:37:14$
 */
package org.chwin.firefighting.apiserver.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

//import com.attilax.web.UrlX;
//import com.attilax.web.sessionImp;
//import com.attilax.collection.list;
//import com.attilax.lang.Global;

/**
 * @author attilax
 *
 */
public class requestImp implements HttpServletRequest {

	/**
	@author attilax 老哇的爪子
		@since  2014-4-28 上午11:37:14$
	
	 * @param args
	 */
	public static void main(String[] args) {
		// 上午11:37:14   2014-4-28 
		requestImp i=new requestImp();
		i.setParam("k1",1);
		System.out.println(i.getParameter("k1"));

	}
	public Map map=new HashMap();
	//ori map
public Map<String ,String[]> map2=new HashMap();
private String ContextPath;
private String ServerName;
private int ServerPort;
private String addr="";
	/**
	@author attilax 老哇的爪子
		@since  2014-4-28 上午11:39:03$
	
	 * @param string
	 * @param i
	 */
	public void setParam(String string, Object i) {
		// 上午11:39:03   2014-4-28 
		this.map.put(string, i.toString());
		
		String[] a=i.toString().split(",");
		this.map2.put(string, a);
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	@Override
	public String getAuthType() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	@Override
	public String getContextPath() {
		// 上午11:38:06   2014-4-28 
		return this.ContextPath;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	@Override
	public Cookie[] getCookies() {
		 
		return cookies_li.toArray(new Cookie[cookies_li.size()]);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	@Override
	public long getDateHeader(String name) {
		// 上午11:38:06   2014-4-28 
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	@Override
	public Enumeration getHeaderNames() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	@Override
	public Enumeration getHeaders(String name) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	@Override
	public int getIntHeader(String name) {
		// 上午11:38:06   2014-4-28 
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	@Override
	public String getMethod() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	@Override
	public String getPathInfo() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	@Override
	public String getPathTranslated() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	@Override
	public String getQueryString() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	@Override
	public String getRemoteUser() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	@Override
	public StringBuffer getRequestURL() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	@Override
	public String getRequestedSessionId() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	@Override
	public String getServletPath() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	@Override
	public HttpSession getSession() {
		// 上午11:38:06   2014-4-28 
		return this.sess;
	}

	public String changeSessionId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	@Override
	public HttpSession getSession(boolean create) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	@Override
	public boolean isRequestedSessionIdFromURL() {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	@Override
	public boolean isRequestedSessionIdValid() {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(String role) {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	@Override
	public Enumeration getAttributeNames() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	@Override
	public String getCharacterEncoding() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	@Override
	public int getContentLength() {
		// 上午11:38:06   2014-4-28 
		return 0;
	}

	@Override
	public long getContentLengthLong() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	@Override
	public String getContentType() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	@Override
	public String getLocalAddr() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	@Override
	public String getLocalName() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	@Override
	public int getLocalPort() {
		// 上午11:38:06   2014-4-28 
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	@Override
	public Locale getLocale() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	@Override
	public Enumeration getLocales() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		// 上午11:38:06   2014-4-28 
		String r=(String) this.map.get(name);
	//	if(r==null)return "";
		if(r==null) return null;
		try {
			r=URLDecoder.decode(r, "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}catch(Exception e)
		{
			
		}
//		if(r==null)
//			return "";
		return r;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	@Override
	public Map getParameterMap() {
		// 上午11:38:06   2014-4-28 
		return this.map2;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	@Override
	public Enumeration getParameterNames() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	@Override
	public String[] getParameterValues(String name) {
		// 上午11:38:06   2014-4-28 
		return map2.get(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	@Override
	public String getProtocol() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	@Override
	public String getRealPath(String path) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	@Override
	public String getRemoteAddr() {
		// 上午11:38:06   2014-4-28 
		return this.addr;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	@Override
	public String getRemoteHost() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	@Override
	public int getRemotePort() {
		// 上午11:38:06   2014-4-28 
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	@Override
	public String getScheme() {
		// 上午11:38:06   2014-4-28 
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	@Override
	public String getServerName() {
		// 上午11:38:06   2014-4-28 
		return this.ServerName;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	@Override
	public int getServerPort() {
		// 上午11:38:06   2014-4-28 
		return this.ServerPort;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	@Override
	public boolean isSecure() {
		// 上午11:38:06   2014-4-28 
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		// 上午11:38:06   2014-4-28 
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object o) {
		// 上午11:38:06   2014-4-28 
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		// 上午11:38:06   2014-4-28 
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAsyncContext()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public AsyncContext getAsyncContext() {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		 
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getDispatcherType()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public DispatcherType getDispatcherType() {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServletContext()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public ServletContext getServletContext() {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isAsyncStarted()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public boolean isAsyncStarted() {
		// attilax 老哇的爪子  0_3_s   o8a 
		return false;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isAsyncSupported()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public boolean isAsyncSupported() {
		// attilax 老哇的爪子  0_3_s   o8a 
		return false;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#startAsync()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public AsyncContext startAsync() throws IllegalStateException {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#startAsync(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#authenticate(javax.servlet.http.HttpServletResponse)
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
		// attilax 老哇的爪子  0_3_s   o8a 
		return false;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPart(java.lang.String)
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public Part getPart(String arg0) throws IOException, ServletException {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getParts()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public Collection<Part> getParts() throws IOException, ServletException {
		// attilax 老哇的爪子  0_3_s   o8a 
		return null;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#login(java.lang.String, java.lang.String)
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public void login(String arg0, String arg1) throws ServletException {
		// attilax 老哇的爪子  0_3_s   o8a 
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#logout()
	 * @author  attilax 老哇的爪子
	 *@since  o8a 0_3_s$
	 */
	@Override public void logout() throws ServletException {
		// attilax 老哇的爪子  0_3_s   o8a 
		
	}

	/**
	@author attilax 老哇的爪子
		@since  o8i 0_t_54   
	
	 * @param string
	 */
	public void setContextPath(String string) {
		// attilax 老哇的爪子  0_t_54   o8i 
		this.ContextPath=string;
	}

	/**
	@author attilax 老哇的爪子
		@since  o8i 0_38_2   
	
	 * @param string
	 */
	public void setServerName(String string) {
		// attilax 老哇的爪子  0_38_2   o8i 
		this.ServerName=string;
	}

	/**
	@author attilax 老哇的爪子
		@since  o8i 0_38_49   
	
	 * @param i
	 */
	public void setServerPort(int i) {
		// attilax 老哇的爪子  0_38_49   o8i 
		this.ServerPort=i;
	}

		/**
		@author attilax 老哇的爪子
		@since   ob8 m_g_41
		 
		 */
	public void setRemoteAddr(String string) {
		this.addr=string;
		
	}

		public void put(String key, String v) {
			this.map.put(key, v);
			String[] vs=new String[1];
			vs[0]=v;
			map2.put(key, vs);
			
		}

		/**
		attilax    2016年4月7日  下午4:41:31
		 * @param m
		 */
		@SuppressWarnings("all")
		public void setParamMap( Map m) {
			Set<String> st=m.keySet();
			for (String k : st) {
				setParam(k, m.get(k));
				
			}
			
		}
		
			public requestImp setQueryString(String s) {
		      String[] a=s.split("&");
		      for (String a_item : a) {
				String[] a2=a_item.split("=");
				setParam(a2[0], a2[1]);
			}
		      return this;
			
		}
 public List<Cookie> cookies_li=new ArrayList<Cookie>();
 public HttpSession sess;
		/**
		attilax    2016年4月19日  下午5:10:44
		 * @param ck
		 */
		public void addCookie(Cookie ck) {
			cookies_li.add(ck);
			
		}

		public void cancelMap() {
			 this.map=new HashMap(); this.map2=new HashMap();
			
		}

		public void setParamByUrl(String s) {
			
//			String queryStr=UrlX.getQueryStr(s);
//			Map  m=UrlX.getHeader_from_QueryStr(queryStr);
//			this.setParamMap(m);
			
		}

		public requestImp setIntoGlobal() {
		//	Global.req.set(this);
			return this;
		}

		/**
		attilax    2016年11月2日  下午5:29:17
		 * @param array
		 */
		public void setcookies(Cookie[] array) {
			// TODO Auto-generated method stub
			
		}
		
		public void setcookies(List<Cookie> li) {
			this.cookies_li=li;
			
		}

		/**
		attilax    2016年11月22日  下午6:33:06
		 * @param si
		 */
		public void setSession(HttpSession si) {
			 this.sess=si;
			
		}

}
