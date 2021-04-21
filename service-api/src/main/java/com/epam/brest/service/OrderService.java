package com.epam.brest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;

/**
 * This interface is implemented by two applications 
 * Rest App and Web App and calls the OrderDAO and ProductService methods
 */
public interface OrderService {

	public void saveOrder(Order theOrder);
	public List<Order> getOrders();
	public void deleteOrder(int id);
	public Optional<Order> getOrderById(int id);
	public void updateOrder(Order theOrder);
	public String getStatusText(int status);
	public Double totalPrice(int orderId);
	HashMap<Integer, Integer> getQuantity(int theId);
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval);
	public boolean checkOrderExist(int id);
}
