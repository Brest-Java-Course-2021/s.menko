package com.epam.brest.service.impl;

import com.epam.brest.model.Category;
import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.OrderService;
import com.epam.brest.testdb.SpringJdbcConfig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({OrderServiceImpl.class,ProductServiceImpl.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@Transactional
class OrderServiceImplIT {

    @Autowired
    OrderService orderService;

    @Test
    public void  getOrdersTest() {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
    }
    
    @Test
    public void getOrderByIdTest() {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);

         Integer orderId = orders.get(0).getId();
         Order expOrder =orderService.getOrderById(orderId).get();
         Assertions.assertEquals(orderId, expOrder.getId());
         Assertions.assertEquals(orders.get(0).getUserName(), expOrder.getUserName());
         Assertions.assertEquals(orders.get(0).getUserPhone(), expOrder.getUserPhone());
         Assertions.assertEquals(orders.get(0).getUserComment(), expOrder.getUserComment());
         Assertions.assertEquals(orders.get(0).getDateTime(), expOrder.getDateTime());
         Assertions.assertEquals(orders.get(0), expOrder);
    }
    
    @Test
    public void getOrderByIdExceptionalTest() {

        Optional<Order> optionalOrder = orderService.getOrderById(999);
        assertTrue(optionalOrder.isEmpty());
    }
    
    @Test
    public void getStatusTest() {

    	Assertions.assertEquals(orderService.getStatusText(4), "Закрыт");
    	Assertions.assertNull(orderService.getStatusText(999));
    }
    
    @Test
    public void totalPriceTest() {
    	Assertions.assertEquals(orderService.totalPrice(1), 1341.9);
    	Assertions.assertNull(orderService.totalPrice(999));
    }
    
    @Test
    public void deleteOrderTest() {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);

        orderService.deleteOrder(2);

        List<Order> realOrders =  orderService.getOrders();
        Assertions.assertEquals(orders.size()-1, realOrders.size());
    }
    
    @Test
    public void getQuantityTest() {
    	Map<Integer, Integer> quantity = new HashMap<Integer, Integer>();
    	quantity.put(2, 3);
    	quantity.put(3, 4);
    	Assertions.assertEquals(orderService.getQuantity(1),quantity);
    	assertTrue(orderService.getQuantity(999).isEmpty());
    	
    }
    
    @Test
    public void checkOrderExistTest() {
        assertTrue(orderService.checkOrderExist(1));
        assertTrue(!orderService.checkOrderExist(999));
    }
    
    @Test
    public void getOrdersFromIntervalTest() {
    	
    	ZonedDateTime startDate = ZonedDateTime.parse("2021-04-06T10:15:30+01:00");
    	ZonedDateTime endDate = ZonedDateTime.parse("2025-03-27T10:15:30+01:00");
    	assertTrue(!orderService.getOrdersFromInterval(new DataOrderInterval(null,null)).isEmpty());
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(startDate,null)).size()>0);
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(startDate,endDate)).size()>0);
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(null,endDate)).size()>0);
    	assertTrue(orderService.getOrdersFromInterval(
    			new DataOrderInterval(startDate.plusYears(6),endDate)).
    			isEmpty());
    }
    
    @Test
    public void saveOrderTest() {
    	
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
        Order newOrder=new Order("Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
        HashMap <Integer, Integer> quantity = new HashMap<Integer, Integer>();
    	quantity.put(3, 3);
    	quantity.put(4, 4);
    	newOrder.setProductsMap(quantity);
        orderService.saveOrder(newOrder);
        List<Order> realOrders = orderService.getOrders();
        Assertions.assertEquals(orders.size() + 1, realOrders.size());
    }
    
    @Test
    public void updateCategoryTest() {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);

        Order order =  orders.get(2);
        order.setUserName("TEST_USERNAME");
        orderService.updateOrder(order);

        Optional<Order> realOrder =  orderService.getOrderById(order.getId());
        Assertions.assertEquals("TEST_USERNAME",realOrder.get().getUserName());
    }
    
   
   
}
