package com.luv2code.springdemo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AdminBase {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	 HttpSession session;
	
	public Integer checkLogged() throws IOException {
	
    Integer userId = (Integer)session.getAttribute("usersId");  
	
    if(userId==null) {
		response.sendRedirect("/web-customer-tracker/user/login");
	}
    
	return  userId;
	}
}
