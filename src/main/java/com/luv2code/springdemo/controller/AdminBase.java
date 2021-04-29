package com.luv2code.springdemo.controller;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

import com.luv2code.springdemo.service.UserService;

public abstract class AdminBase {
	
	@Autowired
	private UserService userService;
	
	public Boolean checkLoggedByAdmin() {
		
	    Optional<Integer> userIdOptional = userService.getIdFromSession();  
	    
	    Boolean result = userIdOptional.isPresent()&&
	    		userService.getUserById(userIdOptional.get()).getRole().equals("admin");
	    
		return  result;
	}
}
