package com.funnyfin.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/*
 * @author KC Kong
 */

public class CookieFilter implements Filter {

	private String cookieNameToFilter = "";

	public void init(FilterConfig filterConfig) throws ServletException { 
		cookieNameToFilter= filterConfig.getInitParameter("cookieName");  
		if (cookieNameToFilter == null) {
			cookieNameToFilter = "";
		}
	}


	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest adjustedRequest = modRequest((HttpServletRequest) req);
		chain.doFilter(adjustedRequest, resp);

	}

	public void destroy() { }

	private HttpServletRequest modRequest(final HttpServletRequest req) {

		return new HttpServletRequestWrapper(req) {

			public Cookie []  getCookies() {
				Cookie [] cookies = req.getCookies();
				ArrayList<Cookie> newCookiesAL = new ArrayList<Cookie> ();
				if (cookies == null) {
					return null;
				}
				for (Cookie cookie: cookies) {
					if (cookie.getName().equals(cookieNameToFilter)) {
						//Skip
					} else {
						newCookiesAL.add(cookie);
					}
				}
				Cookie[] newCookies  = new Cookie[newCookiesAL.size()]; 
				return newCookiesAL.toArray(newCookies);
			}


			public Enumeration<String>  getHeaders(String name) {
				Enumeration<String> headers  = req.getHeaders(name);

				if (! name.equals("cookie"))  {
					return headers;
				}
				if (headers == null) {
					return null;
				}
				Cookie [] originalCookies = req.getCookies();
				Vector<String> newHeaders = new Vector<String> ();
				String newCookieString = "";	
				for (Cookie cookie: originalCookies) {
					if (cookie.getName().equals(cookieNameToFilter)) {
						//Remove this one
					} else {
						newCookieString+= ("".equals(newCookieString)?"":"; ") + cookie.getName() + "=" + cookie.getValue();
					}
				}	
				newHeaders.add(newCookieString);	
				return newHeaders.elements();
			}
		};
	}
}