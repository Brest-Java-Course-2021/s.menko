package com.epam.brest.service;

import java.util.Optional;

import com.epam.brest.model.User;

/**
 * This interface is implemented by two applications 
 * Rest App and Web App and calls the UserDAO methods
 */
public interface UserService {
	
	public boolean checkEmailExist(String email);
	public void saveUser(User user);
	public Integer checkUserData(String email,String password);
	public Optional<User> getUserById(int id);
	public boolean checkEmailWithDifferentIdExist(int id,String email);
	public boolean checkUserExist(int id);
	public Optional<Integer> getIdFromSession();

}
