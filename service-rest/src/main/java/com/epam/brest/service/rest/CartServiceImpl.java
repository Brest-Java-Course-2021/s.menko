package com.epam.brest.service.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.brest.model.Product;
import com.epam.brest.service.CartService;


@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private HttpSession session;
	
	@Override
	public int addProduct(int id) {
		
		HashMap<Integer, Integer> productsMap = new HashMap<>();
		
		if(getProducts().isPresent()) {
			productsMap = getProducts().get();
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
	@Override
	public Integer countItems() {
		
		Optional<HashMap<Integer, Integer>> productsMap = getProducts();
		if(!productsMap.isPresent()) {
			return null;
		}
		else
		{
			int countItems=0;
			for (Integer value : productsMap.get().values()) {
				countItems+=value;
			}
			return countItems;
		}
	}
	@Override
	public Optional<HashMap<Integer, Integer>> getProducts(){
		
		return Optional.ofNullable((HashMap<Integer, Integer>) session.getAttribute("products"));
	}
	@Override
	public double getTotalPrice(List<Product> productsByIds){
		
		HashMap<Integer, Integer> productsMap = getProducts().get();
		
		double TotalPrice=0;

			for(Product i:productsByIds) {
				TotalPrice+=i.getPrice()*productsMap.get(i.getId());
		}
	
		return TotalPrice;
	}
	@Override
		public void removeProductFromCart(int id) {
			
			Optional<HashMap<Integer, Integer>> productsMapOptional = getProducts();
			HashMap<Integer, Integer> productsMap = productsMapOptional.get();
			productsMap.remove(id);
			
			if(productsMap.isEmpty()) {
				productsMap=null;
				productsMapOptional=Optional.ofNullable(productsMap);
			}			
			
			if(!productsMapOptional.isPresent())
				session.setAttribute("cartList", false);
			else
				session.setAttribute("cartList", true);
			
			session.setAttribute("products", productsMap);
			session.setAttribute("countItrems", countItems()); 	
		}
}
