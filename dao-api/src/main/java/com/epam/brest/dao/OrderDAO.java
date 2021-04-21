package com.epam.brest.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.epam.brest.model.Category;
import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;

public interface OrderDAO {
	/**
     * Accesses the database and saves the passed order
     * @return void 
     */
	public void saveOrder(Order theOrder,HashMap<Integer, Integer> productsMap);
	/**
     * Accesses the database and selects all orders
     * @return list of all orders
     */
	public List<Order> getOrders();
	/**
     * Accesses  the database and delete the order by the passed id
     * @return void
     */
	public void deleteOrder(int id);
	/**
     * Accesses  the database and return the order by the passed id
     * @return the resulting order or null wrapped in an Optional
     */
	public Optional<Order> getOrderById(int id);
	/**
     * Accesses the database and update the passed order
     * @return void 
     */
	public void updateOrder(Order theOrder);
	/**
     * Accesses the database and gets the total 
     * cost of the order according to the passed orderId
     * @return value of total cost 
     */
	public  Double totalPrice(int orderId);
	/**
     * Accesses the database and gets the quantity of each item in the order
     * @return HashMap of products ids and their quantity
     */
	HashMap<Integer, Integer> getQuantity(int theId);
	/**
     * Accesses  the database and receives 
     * all orders from a given time interval
     * @return list of orders
     */
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval);
	/**
     * Accesses the database and checks if the given order exists
     * @return boolean true if the order exists, false otherwise
     */
	public boolean checkOrderExist(int id);

}
