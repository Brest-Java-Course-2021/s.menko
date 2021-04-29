package com.luv2code.springdemo.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.DataOrderInterval;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderRestController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/orders")
	public List<Order> getOrders() {	
		
		return orderService.getOrders();
	}
	
	@DeleteMapping("/orders/{orderId}")
	public String deleteOrder(@PathVariable int orderId) {

		orderService.deleteOrder(orderId);
		
		return "Deleted order id - " + orderId;
	}
	
	@GetMapping("/orders/{orderId}")
	public Order getOrderById(@PathVariable int orderId) {
		
		Order theOrder = orderService.getOrderById(orderId);
		
		return theOrder;
	}
	
	@PutMapping("/orders")
	public Order updateOrder(@RequestBody Order theOrder) {
		
		orderService.updateOrder(theOrder);
		return theOrder;
		
	}
	
	@PostMapping("/orders")
	public Order saveOrder(@RequestBody Order theOrder) {
		orderService.saveOrder(theOrder);
		return theOrder;
	}
	
	
	@GetMapping("/orders/totalPrice/{orderId}")
	public Double getTotalPrice(@PathVariable int orderId){	
		
		return orderService.totalPrice(orderId);
	}
	
	@GetMapping("/orders/quantity/{orderId}")
	public HashMap<Integer, Integer> getQuantity(@PathVariable int orderId){	
		
		return orderService.getQuantity(orderId);
	}
	
	@PostMapping("/orders/interval")
	public List<Order> getOrdersFromInterval(@RequestBody DataOrderInterval dataInterval) {	
		return orderService.getOrdersFromInterval(dataInterval);
	}
	
}


















