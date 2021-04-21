package com.epam.brest.service.web_app;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

import com.epam.brest.model.User;
import com.epam.brest.service.UserService;


public abstract class AdminBase {
	
	@Autowired
	private UserService userService;
	
	public Boolean checkLoggedByAdmin() {
		
	    Optional<Integer> userIdOptional = userService.getIdFromSession();  
	    
	    Boolean result = userIdOptional.isPresent()&&
	    		userService.getUserById(userIdOptional.get()).get().getRole().equals("admin");
	    return  result;
	}
}
