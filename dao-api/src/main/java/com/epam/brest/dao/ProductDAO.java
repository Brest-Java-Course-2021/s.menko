package com.epam.brest.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.epam.brest.model.Order;
import com.epam.brest.model.Product;

public interface ProductDAO {
	
	/**
     * Accesses the database and selects last products
     * @return list of last products
     */
	public List<Product> getLatestProducts();
	/**
     * Accesses the database and retrieves a list 
     * of products from a category from a specific page
     * @return list of products
     */
	public List<Product> getProdactsListByCategory(int category,int page);
	/**
     * Accesses the database and return the product by the passed id
     * @return the resulting product or null wrapped in an Optional
     */
	public Optional<Product> getProdactById(int id);
	/**
     * Access the database and gets the total number 
     * of products from the category
     * @return integer product quantity
     */
	public int getTotalProdactsInCategory(int category);
	/**
     * Accesses the database and gets a list of products by the passed ids
     * @return list of products
     */
	public List<Product> getProdactsByIds(Set<Integer> ids);
	/**
     * Accesses the database and selects all products
     * @return list of all products
     */
	public List<Product> getProdactList();
	/**
     * Accesses the database and delete the product by the passed id
     * @return void
     */
	public void deleteProduct(int id);
	/**
     * Accesses the database and saves the passed product
     * @return integer id of the new product 
     */
	public int saveProduct(Product theProduct);
	/**
     * Accesses the database and selects all products from the order
     * @return list of products
     */
	public List<Product> getProdactsFromOrder(int orderId);
	/**
     * Accesses the database and checks if a product 
     * is in the order
     * @return boolean true if the product is in the order, false otherwise
     */
	public boolean productInOrderExist(int idProduct);
	/**
     * Accesses the database and checks if the given product exists
     * @return boolean true if the product exists, false otherwise
     */
	public boolean checkProductExist(int id);

}
