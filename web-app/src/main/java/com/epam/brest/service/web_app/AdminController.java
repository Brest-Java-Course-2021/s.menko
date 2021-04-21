package com.epam.brest.service.web_app;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.brest.service.UserService;



@Controller
@RequestMapping("/admin")
public class AdminController extends AdminBase {
	
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public String showAdminPanel(Model theModel) throws IOException {
		
		
		if(userService.getIdFromSession().isEmpty())
			return "redirect:/user/login";
		
		if(!checkLoggedByAdmin()) {
			theModel.addAttribute("error", "В доступе отказано!");
		}
		return "admin/index";
	}

}
