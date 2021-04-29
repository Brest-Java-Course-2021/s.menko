package com.luv2code.springdemo.service;

import java.util.HashMap;
import java.util.List;

import com.luv2code.springdemo.entity.DataOrderInterval;
import com.luv2code.springdemo.entity.Order;

public interface OrderService {

	public void saveOrder(Order theOrder);
	public List<Order> getOrders();
	public void deleteOrder(int id);
	public Order getOrderById(int id);
	public void updateOrder(Order theOrder);
	public String getStatusText(int status);
	public Double totalPrice(int orderId);
	HashMap<Integer, Integer> getQuantity(int theId);
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval);
}
