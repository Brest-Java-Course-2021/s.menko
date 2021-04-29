package com.luv2code.springdemo.rest;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springdemo.entity.CheckUserData;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private  UserService userService ;
	
	@PostMapping("/users")
	public void addUser(@RequestBody User theUser) {
		 theUser.setId(0);
		 userService.saveUser(theUser);
	}
	
	@GetMapping("/users/{userId}")
	public User getUser(@PathVariable int userId) {
		User theUser = userService.getUserById(userId);
		return theUser;
	}
	
	@PutMapping("/users")
	public void updateUser(@RequestBody User theUser) {
		userService.saveUser(theUser);
		
	}
	
	@PostMapping("/users/emailExist")
	public boolean checkEmailExist(@RequestBody(required = false) String email) {
		return userService.checkEmailExist(email);
	}
	
	@PostMapping("/users/checkData")
	public Optional<User> checkUserData(@RequestBody CheckUserData data) {
		return userService.checkUserData(data.getEmail(), data.getPassword());
	}
	
}


















