package com.epam.brest.service.web_app;


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

import com.epam.brest.model.User;
import com.epam.brest.service.UserService;

//
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;
	
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
	public String userLogout(Model theModel,HttpSession session) {
		
		session.removeAttribute("usersId");
		return "redirect:/";
	}
	
	
	@PostMapping("/processLogin")
	public String processLogin(Model theModel,
			@Valid @ModelAttribute("user") User theUser,
			BindingResult theBindingResult) {
		
		Integer userId =0;
		
		if (!theBindingResult.hasFieldErrors("email")&&!theBindingResult.hasFieldErrors("password")) 
			userId=userService.checkUserData(theUser.getEmail(),theUser.getPassword());
		
		if(userId>0) {
			session.setAttribute("usersId", userId);
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
