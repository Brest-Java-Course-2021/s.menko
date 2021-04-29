package com.luv2code.springdemo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.OrderDAO;
import com.luv2code.springdemo.entity.DataOrderInterval;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderDAO OrderDAO;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	ProductService productService;
	
	@Override
	@Transactional
	public void saveOrder(Order theOrder) {
		
      HashMap<Integer, Integer> productsMap = cartService.getProducts();	
	  Set<Integer> ids=productsMap.keySet();
	  List<Product> productsByIds = productService.getProdactsByIds(ids);
	  theOrder.setProducts(productsByIds);
	  
	 OrderDAO.saveOrder(theOrder,productsMap);
		
	}

	@Override
	@Transactional
	public List<Order> getOrders() {
		// TODO Auto-generated method stub
		return OrderDAO.getOrders();
	}

	@Override
	@Transactional
	public void deleteOrder(int id) {
		OrderDAO.deleteOrder(id);
		
	}

	@Override
	@Transactional
	public Order getOrderById(int id) {
		// TODO Auto-generated method stub
		return OrderDAO.getOrderById(id);
	}

	@Override
	@Transactional
	public void saveOnlyThisOrder(Order theOrder) {
		OrderDAO.saveOnlyThisOrder(theOrder);
		
	}

	@Override
	public String getStatusText(int status) {
		switch (status) {
        case 1:
            return "Новый заказ";   
        case 2:
        	return "В обработке";
            
        case 3:
        	return "Доставляется";
        case 4:
        	return "Закрыт";
        default:
        	return null;    
		}
	}

	@Override
	@Transactional
	public Double totalPrice(int orderId) {
		// TODO Auto-generated method stub
		return OrderDAO.totalPrice(orderId);
	}

	@Override
	@Transactional
	public HashMap<Integer, Integer> getQuantity(int theId) {
		// TODO Auto-generated method stub
		return OrderDAO.getQuantity(theId);
	}

	@Override
	@Transactional
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval) {
		// TODO Auto-generated method stub
		return OrderDAO.getOrdersFromInterval(dataInterval);
	}
	
	
	

}
