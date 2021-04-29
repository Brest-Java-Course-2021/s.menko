package com.luv2code.springdemo.controller;


import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.UserService;

@Controller
@RequestMapping("/user")
@SessionAttributes("usersId")
public class UserController {

	@Autowired
	private UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model theModel) {
		theModel.addAttribute("user", new User());
		return "user/login";
	}
	
	@GetMapping("/logout")
	public String userLogout(HttpSession session,SessionStatus status) {
		
		status.setComplete();
		session.removeAttribute("usersId");
		return "redirect:/";
	}
	
	
	@PostMapping("/processLogin")
	public String processLogin(Model theModel,
			@Valid @ModelAttribute("user") User theUser,
			BindingResult theBindingResult) {
		
		Optional<User> userOptional = Optional.ofNullable(null);
		
		if (!theBindingResult.hasFieldErrors("email")&&!theBindingResult.hasFieldErrors("password")) 
			userOptional=userService.checkUserData(theUser.getEmail(),theUser.getPassword());
		
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			theModel.addAttribute("usersId", user.getId());
			return "redirect:"+"/cabinet";
		}
		else {
			theModel.addAttribute("error", "No such user exists!");
			return "user/login";
		}
			
	}
	
	@GetMapping("/register")
	public String showForm(Model theModel) {
		theModel.addAttribute("user", new User());
		theModel.addAttribute("register", false);
		theModel.addAttribute("notEmailExist",true);
		return "user/register";
	}
	
	@PostMapping("/processForm")
	public String processForm(Model theModel,
			@Valid @ModelAttribute("user") User theUser,
			BindingResult theBindingResult) {
		
		boolean notEmailExist= userService.checkEmailExist(theUser.getEmail());
		
		if (theBindingResult.hasErrors()||!notEmailExist) {
			theModel.addAttribute("register", false);
			theModel.addAttribute("user",theUser);
			theModel.addAttribute("notEmailExist",notEmailExist);
		}
		else {
			theUser.setRole("user");
			userService.saveUser(theUser);
			theModel.addAttribute("register", true);
		}
		
		return  "user/register";
	}
}
