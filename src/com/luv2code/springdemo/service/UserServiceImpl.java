package com.luv2code.springdemo.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.CategoryDAO;
import com.luv2code.springdemo.dao.UserDAO;
import com.luv2code.springdemo.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO UserDAO;
	
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
	public User checkUserData(String email, String password) {
		// TODO Auto-generated method stub
		return UserDAO.checkUserData(email, password);
	}

	@Override
	@Transactional
	public User getUserById(int id) {
		return UserDAO.getUserById(id);
	}


}
