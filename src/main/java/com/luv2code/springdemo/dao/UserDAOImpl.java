package com.luv2code.springdemo.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.rest.exception.NotFoundException;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@Override
	public boolean checkEmailExist(String email) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.email=:email",User.class);
		
		theQuery.setParameter("email", email);
		
		List<User> users = theQuery.getResultList();
		
		
		return !users.isEmpty();
	}

	@Override
	public void saveUser(User user) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		currentSession.saveOrUpdate(user);
		
	}

	@Override
	public Optional<User> checkUserData(String email, String password) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.email=:email",User.class);
		
		
		theQuery.setParameter("email", email);
		List<User> users= theQuery.getResultList();
		if(users.isEmpty()||!passwordEncoder.matches(password,users.get(0).getPassword()))
			return Optional.ofNullable(null);
		else {
			return Optional.ofNullable(users.get(0));	
		}
	}
	
	@Override
	public Optional<User> getUserById(int id) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		User tempUser = currentSession.get(User.class, id);
		
		return Optional.ofNullable(tempUser);
	}

	@Override
	public boolean checkEmailWithIdExist(String email, int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.email=:email and e.id!=:id",User.class);
		
		theQuery.setParameter("email", email);
		theQuery.setParameter("id", id);
		
		List<User> users = theQuery.getResultList();
		
		
		return !users.isEmpty();
	}

	@Override
	public boolean checkUserExist(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.id=:id",User.class);
		
		theQuery.setParameter("id", id);
		
		return !theQuery.getResultList().isEmpty();
	}
	
	
	
}
