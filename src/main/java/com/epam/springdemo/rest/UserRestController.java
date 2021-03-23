package com.luv2code.springdemo.rest;


import java.util.Optional;

import javax.validation.Valid;

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
import com.luv2code.springdemo.rest.exception.NotFoundException;
import com.luv2code.springdemo.rest.exception.ValidationException;
import com.luv2code.springdemo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private  UserService userService ;
	
	@PostMapping("/users")
	public User addUser(@Valid @RequestBody User theUser) {
		 theUser.setId(0);
		 if(userService.checkEmailExist(theUser.getEmail()))
				throw new ValidationException("Такой email уже существует!");
		 userService.saveUser(theUser);
		 return theUser;
	}
	
	@GetMapping("/users/{userId}")
	public User getUser(@PathVariable int userId) {
		User theUser = userService.getUserById(userId).orElseThrow(() -> new NotFoundException("User id not found - " + userId));
		return theUser;
	}
	
	@PutMapping("/users")
	public User updateUser(@Valid @RequestBody User theUser) {
		if(!userService.checkUserExist(theUser.getId()))
			throw new NotFoundException("User id not found - " + theUser.getId());
		if(userService.checkEmailWithIdExist(theUser.getEmail(),theUser.getId()))
			throw new ValidationException("Такой email уже существует!");
		userService.saveUser(theUser);
		return theUser;
		
	}
	
	@PostMapping("/users/emailExist")
	public boolean checkEmailExist(@RequestBody(required = false) String email) {
		return userService.checkEmailExist(email);
	}
	
	@PostMapping("/users/checkData")
	public Optional<User> checkUserData(@RequestBody CheckUserData data) {
		Optional<User> user=userService.checkUserData(data.getEmail(), data.getPassword());
		return user;
	}
	
}


















