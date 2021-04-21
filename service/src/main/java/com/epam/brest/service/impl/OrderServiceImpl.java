package com.epam.brest.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.model.Product;
import com.epam.brest.service.OrderService;
import com.epam.brest.service.ProductService;
import com.epam.brest.dao.OrderDAO;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderDAO OrderDAO;

	
	@Autowired
	ProductService productService;
	
	@Override
	@Transactional
	public void saveOrder(Order theOrder) {
		
      HashMap<Integer, Integer> productsMap = theOrder.getProductsMap();	
	  Set<Integer> ids=productsMap.keySet();
	  List<Product> productsByIds = productService.getProdactsByIds(ids);
	  theOrder.setProducts(productsByIds);
	  
	 OrderDAO.saveOrder(theOrder,productsMap);
		
	}
	
	@Override
	@Transactional
	public List<Order> getOrders() {
		return OrderDAO.getOrders();
	}
	
	@Override
	@Transactional
	public void deleteOrder(int id) {
		OrderDAO.deleteOrder(id);
		
	}
	
	@Override
	@Transactional
	public Optional<Order> getOrderById(int id) {
		return OrderDAO.getOrderById(id);
	}

	@Override
	@Transactional
	public void updateOrder(Order theOrder) {
		OrderDAO.updateOrder(theOrder);
		
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
		return OrderDAO.totalPrice(orderId);
	}
	
	@Override
	@Transactional
	public HashMap<Integer, Integer> getQuantity(int theId) {
		return OrderDAO.getQuantity(theId);
	}
	
	@Override
	@Transactional
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval) {
		return OrderDAO.getOrdersFromInterval(dataInterval);
	}
	
	@Override
	@Transactional
	public boolean checkOrderExist(int id) {
		return OrderDAO.checkOrderExist(id);
	}
	
	
	

}
