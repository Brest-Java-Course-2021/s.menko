package com.luv2code.springdemo.service;

import java.util.Optional;

import com.luv2code.springdemo.entity.User;

public interface UserService {
	
	public boolean checkEmailExist(String email);
	public void saveUser(User user);
	public Optional<User> checkUserData(String email,String password);
	public Optional<User> getUserById(int id);
	public Optional<Integer> getIdFromSession();
	public boolean checkEmailWithIdExist(String email,int id);
	public boolean checkUserExist(int id);

}
