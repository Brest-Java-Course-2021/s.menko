package com.luv2code.springdemo.dao;


import java.util.Optional;

import com.luv2code.springdemo.entity.User;

public interface UserDAO {
	
	
	public boolean checkEmailExist(String email);
	public void saveUser(User user);
	public Optional<User> checkUserData(String email,String password);
	public Optional<User> getUserById(int id);
	public boolean checkEmailWithIdExist(String email, int id);
	public boolean checkUserExist(int id);
}
