package com.luv2code.springdemo.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.rest.exception.NotFoundException;

@Repository
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	
	final int COUNT=6;
	
	@Override
	public List<Product> getLatestProducts() {
		

		Session currentSession = sessionFactory.getCurrentSession();
		Query<Product> theQuery = 
				currentSession.createQuery("FROM Product e WHERE e.status=1 ORDER BY e.id DESC",Product.class);
		
		theQuery.setMaxResults(COUNT);
		
		List<Product> products = theQuery.getResultList();
			
		return  products;
	}
	
	@Override
	public List<Product> getProdactsListByCategory(int category,int page){
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Product> theQuery = 
				currentSession.createQuery("FROM Product e WHERE e.status=1 AND e.category_id=:categoryId ORDER BY e.id DESC",Product.class);
		
		theQuery.setParameter("categoryId", category);
		theQuery.setMaxResults(COUNT);
		theQuery.setFirstResult((page-1)*COUNT);
		
		List<Product> products = theQuery.getResultList();
				
		return  products;
		
		
	}
	
	@Override
	public Optional<Product> getProdactById(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Product tempProduct = currentSession.get(Product.class, id);
		
		return Optional.ofNullable(tempProduct);
	}

	@Override
	public int getTotalProdactsInCategory(int category) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Long> theQuery = 
				currentSession.createQuery("select count(e) FROM Product e WHERE e.status=1 AND e.category_id=:categoryId",Long.class);
		
		theQuery.setParameter("categoryId", category);

		int result = theQuery.getResultList().get(0).intValue();	
		return  result;

	}

	@Override
	public List<Product> getProdactsByIds(Set<Integer> ids) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Product> theQuery = 
				currentSession.createQuery("FROM Product e WHERE e.id IN (:ids)",Product.class);
		
		theQuery.setParameter("ids", ids);
		
		List<Product> products = theQuery.getResultList();
		
		return products;
	}

	@Override
	public List<Product> getProdactList() {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Product> theQuery = 
				currentSession.createQuery("FROM Product e ORDER BY e.id ASC",Product.class);
		List<Product> products = theQuery.getResultList();
		return products;
	}

	@Override
	public void deleteProduct(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = 
				currentSession.createQuery("delete from Product where id=:productId");
		theQuery.setParameter("productId", id);
		
		theQuery.executeUpdate();		
		
	}

	@Override
	public int saveProduct(Product theProduct) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theProduct);
		return theProduct.getId();
		
	}

	@Override
	public List<Product> getProdactsFromOrder(int orderId) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Order tempOrder = currentSession.get(Order.class, orderId);
		tempOrder.getProducts().size();
		return tempOrder.getProducts();
	}

	@Override
	public boolean productInOrderExist(int idProduct) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("select count(*) from OrderDetail where productId=:id");
		theQuery.setParameter("id",idProduct);
		boolean exist=(Long)theQuery.uniqueResult()>0;
		return exist;
	}

	@Override
	public boolean checkProductExist(int id) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Product> theQuery = 
				currentSession.createQuery("FROM Product e WHERE e.id=:id",Product.class);
		
		theQuery.setParameter("id", id);
		
		return !theQuery.getResultList().isEmpty();
	}

	

}
