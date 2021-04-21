package com.epam.brest.service.rest_app;


import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.brest.model.CheckUserData;
import com.epam.brest.model.User;
import com.epam.brest.service.UserService;
import com.epam.brest.service.impl.UserServiceImpl;
import com.epam.brest.service.rest_app.exception.NotFoundException;
import com.epam.brest.service.rest_app.exception.ValidationException;


@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private  UserService userService ;
	
	@PostMapping("/users")
	public Integer addUser(@Valid @RequestBody User theUser) {
		 theUser.setId(0);
		 if(userService.checkEmailExist(theUser.getEmail()))
				throw new ValidationException("Такой email уже существует!");
		 userService.saveUser(theUser);
		 return theUser.getId();
	}
	
	@GetMapping("/users/{userId}")
	public User getUser(@PathVariable int userId) {
		User theUser = userService.getUserById(userId).orElseThrow(() -> new NotFoundException("User id not found - " + userId));
		theUser.setPassword("Недоступно");
		return theUser;
	}
	
	@PutMapping("/users")
	public Integer updateUser(@Valid @RequestBody User theUser) {
		if(!userService.checkUserExist(theUser.getId()))
			throw new NotFoundException("User id not found - " + theUser.getId());
		if(userService.checkEmailWithDifferentIdExist(theUser.getId(),theUser.getEmail()))
			throw new ValidationException("Такой email уже существует!");
		userService.saveUser(theUser);
		return theUser.getId();
		
	}
	
	@PostMapping("/users/existenceOfEmail")
	public boolean checkEmailExist(@RequestBody(required = false) String email) {
		return userService.checkEmailExist(email);
	}
	
	@PostMapping("/users/dataCheck")
	public Integer checkUserData(@RequestBody CheckUserData data) {
		Integer userId=userService.checkUserData(data.getEmail(), data.getPassword());
		return userId;
	}
	
	@GetMapping("/checkEmailWithId/{userId}/{email}")
	public boolean checkEmailWithIdExist(@PathVariable int userId, @PathVariable String email) {
		return userService.checkEmailWithDifferentIdExist(userId,email);
	}

	
	@GetMapping("/checkUser/{userId}")
	public boolean checkUserExist(@PathVariable int userId) {
		return  userService.checkUserExist(userId);
	}
	
}


















