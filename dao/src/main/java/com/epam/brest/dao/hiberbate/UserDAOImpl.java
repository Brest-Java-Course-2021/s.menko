package com.epam.brest.dao.hiberbate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.epam.brest.dao.UserDAO;
import com.epam.brest.model.User;

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
		System.out.println(passwordEncoder.encode("123456"));
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		currentSession.saveOrUpdate(user);
		
	}

	@Override
	public Integer checkUserData(String email, String password) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.email=:email",User.class);
		
		
		theQuery.setParameter("email", email);
		List<User> users= theQuery.getResultList();
		if(users.isEmpty()||!passwordEncoder.matches(password,users.get(0).getPassword()))
			return 0;
		else {
			return users.get(0).getId();	
		}
	}
	
	@Override
	public Optional<User> getUserById(int id) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		User tempUser = currentSession.get(User.class, id);
		
		return Optional.ofNullable(tempUser);
	}

	@Override
	public boolean checkEmailWithDifferentIdExist(int id,String email) {
		
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
