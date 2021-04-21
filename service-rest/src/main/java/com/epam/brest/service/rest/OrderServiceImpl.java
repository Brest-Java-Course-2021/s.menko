package com.epam.brest.service.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.service.OrderService;


@Service
public class OrderServiceImpl implements OrderService {
	
	
	private RestTemplate restTemplate;
	private String crmRestUrl="http://localhost:8080/api/orders";
	
	@Autowired
	public OrderServiceImpl(RestTemplate theRestTemplate) {
		 restTemplate=theRestTemplate;
	}
	
	@Override
	public void saveOrder(Order theOrder) {
		
		restTemplate.postForEntity(crmRestUrl, theOrder, String.class);
		
	}

	@Override
	public List<Order> getOrders() {
		
		ResponseEntity<List<Order>> responseEntity = 
				restTemplate.exchange(crmRestUrl, HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Order>>() {});
		List<Order> orders = responseEntity.getBody();

		return orders;
	}

	@Override
	public void deleteOrder(int id) {
		
		restTemplate.delete(crmRestUrl + "/" + id);
		
	}

	@Override
	public Optional<Order> getOrderById(int id) {
		
		Order theOrder= 
				restTemplate.getForObject(crmRestUrl + "/" + id, 
						Order.class);
		
		return Optional.ofNullable(theOrder);
	}

	@Override
	public void updateOrder(Order theOrder) {
		
		restTemplate.put(crmRestUrl, theOrder);
		
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
	public Double totalPrice(int orderId) {
		
		Double totalPrice= 
				restTemplate.getForObject(crmRestUrl + "/totalPrice/" + orderId, 
						Double.class);
		
		return totalPrice;
	}

	@Override
	public HashMap<Integer, Integer> getQuantity(int theId) {
		
		ResponseEntity<HashMap<Integer, Integer>> responseEntity = 
				restTemplate.exchange(crmRestUrl+"/quantity/"+theId, HttpMethod.GET, null, 
									  new ParameterizedTypeReference<HashMap<Integer, Integer>>() {});
		HashMap<Integer, Integer> quantity = responseEntity.getBody();

		return quantity;
	}

	@Override
	public List<Order> getOrdersFromInterval(DataOrderInterval dataInterval) {
		
		
		RequestEntity<DataOrderInterval> request = RequestEntity
	            .post(URI.create(crmRestUrl+"/interval"))
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(dataInterval);
		ResponseEntity<List<Order>> response = restTemplate.exchange(
				crmRestUrl+"/interval", 
	            HttpMethod.POST, 
	            request, 
	            new ParameterizedTypeReference<List<Order>>() {}
	            );
		
	return response.getBody();
	}

	@Override
	public boolean checkOrderExist(int id) {
			
			Boolean result = 
					restTemplate.getForObject(crmRestUrl+"/orderCheck/" + id,Boolean.class);
			return result;
		
	}


}
