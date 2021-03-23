package com.luv2code.springdemo.service;


import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.UserDAO;
import com.luv2code.springdemo.entity.User;

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
	public Optional<User> checkUserData(String email, String password) {
		return UserDAO.checkUserData(email, password);
	}

	@Override
	@Transactional
	public Optional<User> getUserById(int id) {
		return UserDAO.getUserById(id);
	}

	@Override
	public Optional<Integer> getIdFromSession() {
		return Optional.ofNullable((Integer)session.getAttribute("usersId"));
	}

	@Transactional
	@Override
	public boolean checkEmailWithIdExist(String email, int id) {
		return UserDAO.checkEmailWithIdExist(email,id);
	}

	@Override
	@Transactional
	public boolean checkUserExist(int id) {
		// TODO Auto-generated method stub
		return UserDAO.checkUserExist(id);
	}


}
