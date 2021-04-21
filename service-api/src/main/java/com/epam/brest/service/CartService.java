package com.epam.brest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.epam.brest.model.Product;

/**
 * This interface is implemented only in Web App application
 */
public interface CartService {
	
	public int addProduct(int id);
	public Integer countItems();
	public Optional<HashMap<Integer, Integer>> getProducts();
	public double getTotalPrice(List<Product> productsByIds);
	public void removeProductFromCart(int id);

}
