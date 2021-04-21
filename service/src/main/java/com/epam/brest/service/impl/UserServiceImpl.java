package com.epam.brest.service.impl;


import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.epam.brest.model.User;
import com.epam.brest.service.UserService;
import com.epam.brest.dao.UserDAO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO UserDAO;
	
	@Autowired
	private HttpSession session;
	
	
	@Override
	@Transactional
	public boolean checkEmailExist(String email) {
		return UserDAO.checkEmailExist(email);
	}
	
	@Override
	@Transactional
	public void saveUser(User user) {
		 UserDAO.saveUser(user);
	}
	
	@Override
	@Transactional
	public Integer checkUserData(String email, String password) {
		return UserDAO.checkUserData(email, password);
	}
	
	@Override
	@Transactional
	public Optional<User> getUserById(int id) {
		return UserDAO.getUserById(id);
	}
	
	@Override
	@Transactional
	public boolean checkEmailWithDifferentIdExist(int id, String email) {
		return UserDAO.checkEmailWithDifferentIdExist(id,email);
	}
	
	@Override
	@Transactional
	public boolean checkUserExist(int id) {
		return UserDAO.checkUserExist(id);
	}
	
	@Override
	public Optional<Integer> getIdFromSession() {
		return Optional.ofNullable((Integer)session.getAttribute("usersId"));
	}


}
