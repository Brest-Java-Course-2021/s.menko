package com.luv2code.springdemo.dao;

import javax.servlet.http.HttpSession;

import com.luv2code.springdemo.entity.User;

public interface UserDAO {
	
	
	public boolean checkEmailExist(String email);
	public void saveUser(User user);
	public User checkUserData(String email,String password);
	public User getUserById(int id);
}
