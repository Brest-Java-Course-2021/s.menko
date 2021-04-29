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
		
		
		return users.isEmpty();
	}

	@Override
	public void saveUser(User user) {
		
		Session currentSession = sessionFactory.getCurrentSession();
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
	public User getUserById(int id) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		User tempUser = currentSession.get(User.class, id);
		
		return tempUser;
	}
	
	
	
}
