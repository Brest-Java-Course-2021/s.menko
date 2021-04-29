package com.luv2code.springdemo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.UserService;

import net.bytebuddy.asm.Advice.Exit;

@Controller
@RequestMapping("/admin")
public class AdminController extends AdminBase {
	
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public String showAdminPanel() throws IOException {
		
		Integer userId = checkLogged();
		
		if(userId!=null) {
		User theUser = userService.getUserById(userId);

		String role = theUser.getRole();

		if(role==null||!role.equals("admin")) {
			session.setAttribute("Error", "В доступе отказано!");
		}
		}
		return "admin/index";
	}

}
