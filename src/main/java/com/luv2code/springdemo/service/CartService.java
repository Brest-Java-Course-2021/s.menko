package com.luv2code.springdemo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springdemo.entity.Product;

@Service
public class CartService {

	@Autowired
	private HttpSession session;
	
	public int addProduct(int id) {
		
		HashMap<Integer, Integer> productsMap = new HashMap<>();
		
		if(session.getAttribute("products")!=null) {
			productsMap = (HashMap<Integer, Integer>) session.getAttribute("products");
		}
		
		if(productsMap.containsKey(id)) {
			int currentCount = productsMap.get(id)+1;
			productsMap.replace(id, currentCount);
		}
		else {
			productsMap.put(id, 1); 
		}
		session.setAttribute("products", productsMap);
		session.setAttribute("countItrems", countItems()); 
		return countItems();
		
	}
	
	public Integer countItems() {
		
		HashMap<Integer, Integer> productsMap = (HashMap<Integer, Integer>) session.getAttribute("products");
		if(productsMap.isEmpty()) {
			return null;
		}
		else
		{
			int countItems=0;
			for (Integer value : productsMap.values()) {
				countItems+=value;
			}
			return countItems;
		}
	}
	
	public HashMap<Integer, Integer> getProducts(){
		
		return (HashMap<Integer, Integer>) session.getAttribute("products");
	}
	
	public double getTotalPrice(List<Product> productsByIds){
		
		HashMap<Integer, Integer> productsMap = getProducts();
		
		double TotalPrice=0;

			for(Product i:productsByIds) {
				TotalPrice+=i.getPrice()*productsMap.get(i.getId());
		}
	
		return TotalPrice;
	}
		
		public void removeProductFromCart(int id) {
			
			HashMap<Integer, Integer> productsMap = getProducts();
			productsMap.remove(id);
			
			if(productsMap==null||productsMap.isEmpty())
				session.setAttribute("cartList", false);
			else
				session.setAttribute("cartList", true);
			
			session.setAttribute("products", productsMap);
			session.setAttribute("countItrems", countItems()); 
			
		}

}
