package com.luv2code.springdemo.service;

import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.entity.User;

public interface UserService {
	
	public boolean checkEmailExist(String email);
	public void saveUser(User user);
	public User checkUserData(String email,String password);
	public User getUserById(int id);

}
