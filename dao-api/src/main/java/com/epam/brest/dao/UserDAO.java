package com.epam.brest.dao;


import java.util.Optional;

import com.epam.brest.model.User;

public interface UserDAO {
	
	/**
     * Accesses the database and checks if the given email exists
     * @return boolean true if the email exists, false otherwise
     */
	public boolean checkEmailExist(String email);
	/**
     * Accesses the database and saves the passed User
     * @return void
     */
	public void saveUser(User user);
	/**
     * Accesses the database and checks if the user exists with the 
     * passed email address and password
     * @return returns integer 0 if the user is not found, otherwise the id of the given user
     */
	public Integer checkUserData(String email,String password);
	/**
     * Accesses the database and return the User by the passed id
     * @return the resulting user or null wrapped in an Optional
     */
	public Optional<User> getUserById(int id);
	/**
     * Accesses the database and checks if a user exists 
     * with an email address and an id different from the one given
     * @return boolean true if the user exists, false otherwise
     */
	public boolean checkEmailWithDifferentIdExist( int id,String email);
	/**
     * Accesses the database and checks if the given user exists
     * @return boolean true if the user exists, false otherwise
     */
	public boolean checkUserExist(int id);
}
