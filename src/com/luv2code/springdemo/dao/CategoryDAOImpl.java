package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Category> getCategories() {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		/*Order s = currentSession.get(Order.class, 1);
		System.out.println(s);
		
		Order s1= new Order("Мой сейв","12312332123","asadsd",s.getDateTime());
		currentSession.save(s1);*/
		
		Query<Category> theQuery = 
				currentSession.createQuery("from Category order by sortOrder",
						Category.class);
		
		
		List<Category> categories = theQuery.getResultList();
		
		// return the results		
		return categories;
	}

	@Override
	public void saveCategory(Category theCategory) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theCategory);
		
	}
	
	@Override
	public void deleteCategory(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		//Query theQuery = 
				//currentSession.createQuery("delete from Category where id=:categoryId");
		//theQuery.setParameter("categoryId", id);
		
		Category tempCategory =currentSession.get(Category.class, id);
		
		// delete course

		
		currentSession.delete(tempCategory);
		
		//theQuery.executeUpdate();		
		
	}

	@Override
	public Category getCategorytById(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Category tempCategory = currentSession.get(Category.class, id);
		
		return tempCategory;
	}

}
