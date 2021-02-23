package com.funnyfin.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class HelloWorld extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }
 
    private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {     
    	PrintWriter out = res.getWriter();
        res.setContentType("text/plain");
 
        Enumeration<String> headerNames = req.getHeaderNames();
 
        out.write("----Get Headers----\n");
        while (headerNames.hasMoreElements()) {
 
            String headerName = headerNames.nextElement();
            out.write(headerName);
            out.write("\n");
 
            Enumeration<String> headers = req.getHeaders(headerName);
            
            if (headers !=null) {
            	while (headers.hasMoreElements()) {
            		String headerValue = headers.nextElement();
            		out.write("\t" + headerValue);
            		out.write("\n");
            	}
            }
 
        }
 
        out.write("\n----Get Cookies----\n");
        
        Cookie [] cookies = req.getCookies();
        
        if (cookies !=null) {
        	for (Cookie cookie: cookies) {
        		String name = cookie.getName();
        		String value = cookie.getValue();
        		out.write( name + "\n\t" + value);
        		out.write("\n");
        	}
        }
        out.close();
    }
}