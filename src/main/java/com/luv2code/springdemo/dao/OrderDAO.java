package com.luv2code.springdemo.dao;

import java.util.HashMap;
import java.util.List;

import com.luv2code.springdemo.entity.DataOrderInterval;
import com.luv2code.springdemo.entity.Order;

public interface OrderDAO {
	
	public void saveOrder(Order theOrder,HashMap<Integer, Integer> productsMap);
	public List<Order> getOrders();
	public void deleteOrder(int id);
	public Order getOrderById(int id);
	public void updateOrder(Order theOrder);
	public  Double totalPrice(int orderId);
	HashMap<Integer, Integer> getQuantity(int theId);
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval);

}
