package com.luv2code.springdemo.dao;

import java.util.List;
import java.util.Set;

import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;

public interface ProductDAO {
	
	public List<Product> getLatestProducts();

	public List<Product> getProdactsListByCategory(int category,int page);
	
	public Product getProdactById(int id);
	
	public int getTotalProdactsInCategory(int category);
	
	public List<Product> getProdactsByIds(Set<Integer> ids);
	
	public List<Product> getProdactList();
	
	public void deleteProduct(int id);
	
	public int saveProduct(Product theProduct);
	
	public List<Product> getProdactsFromOrder(int orderId);
	
	public boolean productInOrderExist(int idProduct);

}
