package com.epam.brest.dao.hiberbate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.brest.dao.CategoryDAO;
import com.epam.brest.model.Category;
import com.epam.brest.model.Product;


@Repository
public class CategoryDAOImpl implements CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Category> getCategories() {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Category> theQuery = 
				currentSession.createQuery("from Category order by sortOrder",
						Category.class);
		
		List<Category> categories = theQuery.getResultList();		
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
		
		Category tempCategory =currentSession.get(Category.class, id);
		
		currentSession.delete(tempCategory);	
		
	}

	@Override
	public Optional<Category> getCategoryById(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Category tempCategory = currentSession.get(Category.class, id);
		
		return Optional.ofNullable(tempCategory);
	}

	@Override
	public boolean productOfCategoryInOrderExist(int idCategory) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Set<Integer> ids = new HashSet<>();
		Category tempCategory = getCategoryById(idCategory).get();
		tempCategory.getProducts().size();
		for(Product product:tempCategory.getProducts())
			ids.add(product.getId());
		if(ids.isEmpty()) 
			return false;
		Query theQuery = currentSession.createQuery("select count(*) from OrderDetail where productId IN (:ids)");
		theQuery.setParameter("ids",ids);
		boolean exist=(Long)theQuery.uniqueResult()>0;
		return exist;
	}

	@Override
	public boolean checkCategoryExist(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Category> theQuery = 
				currentSession.createQuery("FROM Category e WHERE e.id=:id",Category.class);
		
		theQuery.setParameter("id", id);
		return !theQuery.getResultList().isEmpty();
	}

}
