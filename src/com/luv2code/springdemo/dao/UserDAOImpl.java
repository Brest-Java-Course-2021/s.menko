package com.luv2code.springdemo.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.entity.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
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
	public User checkUserData(String email, String password) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = 
				currentSession.createQuery("FROM User e WHERE e.email=:email AND e.password=:password",User.class);
		
		theQuery.setParameter("email", email);
		theQuery.setParameter("password", password);

		
		List<User> users= theQuery.getResultList();
		
		if(users.isEmpty())
			return null;
		else
			return users.get(0);

	}
	
	@Override
	public User getUserById(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		User tempUser = currentSession.get(User.class, id);
		
		return tempUser;
	}
	
	
	
}
