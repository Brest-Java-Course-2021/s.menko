package com.epam.brest.service.rest_app;

import com.epam.brest.dao.hiberbate.CategoryDAOImpl;
import com.epam.brest.model.Category;
import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.model.Product;
import com.epam.brest.service.impl.CategoryServiceImpl;
import com.epam.brest.service.rest_app.exception.RestExceptionHandler;
import com.epam.brest.testdb.SpringJdbcConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class OrderControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderControllerIT.class);

    @Autowired
    private OrderRestController orderController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockOrderService orderService = new  MockOrderService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(orderController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(restExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void  getOrdersTest() throws Exception {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);
    }
    
    @Test
    public void deleteOrderTest() throws Exception {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);

        orderService.deleteOrder(2);

        List<Order> realOrders =  orderService.getOrders();
        Assertions.assertEquals(orders.size()-1, realOrders.size());
    }
    
    @Test
    public void shouldReturnNotFoundOrderOnDelete() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(
        		"/api/orders/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void getOrderByIdTest() throws Exception {
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
    }
    
    @Test
    public void shouldReturnNotFoundOnGetOrder() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/orders/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void saveOrderTest() throws Exception {
    	
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
    public void updateOrderTest() throws Exception {
    	List<Order> orders = orderService.getOrders();
        assertNotNull(orders);
        assertTrue(orders.size() > 0);

        Order order =  orders.get(1);
        order.setUserName("TEST_USERNAME");
        orderService.updateOrder(order);

        Optional<Order> realOrder =  orderService.getOrderById(order.getId());
        Assertions.assertEquals("TEST_USERNAME",realOrder.get().getUserName());
    }
    
    @Test
    public void shouldReturnNotFoundOnUpdateOrder() throws Exception {

    	 Order order=new Order("Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
    	 order.setId(999);
    			MockHttpServletResponse response =
                mockMvc.perform(put("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                        .andReturn().getResponse();
        assertNotNull(response);
       }
    
    @Test
    public void totalPriceTest() throws Exception {
    	Assertions.assertEquals(orderService.totalPrice(1), 1341.9);
    }
    
    @Test
    public void shouldReturnNotFoundOnTotalPriceOrder() throws Exception {

    	 MockHttpServletResponse response = mockMvc.perform(get("/api/orders/totalPrice/999")
                 .accept(MediaType.APPLICATION_JSON)
         ).andExpect(status().isNotFound())
                 .andReturn().getResponse();
        assertNotNull(response);
       }
    
    @Test
    public void checkOrderExistTest() throws Exception {
        assertTrue(orderService.checkOrderExist(1));
        assertTrue(!orderService.checkOrderExist(999));
    }
    
    @Test
    public void getQuantityTest() throws Exception {
    	Map<Integer, Integer> quantity = new HashMap<Integer, Integer>();
    	quantity.put(2, 3);
    	quantity.put(3, 4);
    	Assertions.assertEquals(orderService.getQuantity(1),quantity);
    	
    }
    
    @Test
    public void shouldReturnNotFoundOrderOnGetQuantity() throws Exception {

    	MockHttpServletResponse response = mockMvc.perform(get("/api/orders/quantity/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
       }
    
    @Test
    public void getOrdersFromIntervalTest() throws Exception {
    	
    	ZonedDateTime startDate = ZonedDateTime.parse("2021-04-06T10:15:30+01:00");
    	ZonedDateTime endDate = ZonedDateTime.parse("2025-03-27T10:15:30+01:00");
    	assertTrue(!orderService.getOrdersFromInterval(new DataOrderInterval(null,null)).isEmpty());
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(startDate,null)).size()>0);
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(startDate,endDate)).size()>0);
    	assertTrue(orderService.getOrdersFromInterval(new DataOrderInterval(null,endDate)).size()>0);
    }
    @Test
    public void shouldReturnUnprocessableEntityFromIntervalTest() throws Exception {
    	
    	ZonedDateTime startDate = ZonedDateTime.parse("2026-04-06T10:15:30+01:00");
    	ZonedDateTime endDate = ZonedDateTime.parse("2021-03-27T10:15:30+01:00");
    	
    	String json = objectMapper.writeValueAsString(new DataOrderInterval(startDate,endDate));
        MockHttpServletResponse response =
                mockMvc.perform(post("/api/orders/interval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                        .andReturn().getResponse();
        assertNotNull(response);
    }
    /*
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
    */
    /////////////////////////////////////////////////////////////////////////////////

    private class MockOrderService {
    	
    	 public List<Order> getOrders() throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/orders")
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             assertNotNull(response);
            

             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Order>>() {});
         }
    	 
    	 public String deleteOrder (Integer orderId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(
                     MockMvcRequestBuilders.delete("/api/orders/"  + orderId)
                             .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();

             return objectMapper.readValue(response.getContentAsString(), String.class);
         }
    	 
    	 public Optional<Order> getOrderById(Integer orderId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/orders/"  + orderId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return Optional.of(objectMapper.readValue(response.getContentAsString(), Order.class));
         }
    	 
    	 public Order saveOrder(Order order) throws Exception {
             String json = objectMapper.writeValueAsString(order);
             MockHttpServletResponse response =
                     mockMvc.perform(post("/api/orders/")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json)
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Order.class);
         }
    	 
    	 public Order updateOrder(Order order) throws Exception {
             MockHttpServletResponse response =
                     mockMvc.perform(put("/api/orders/")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(objectMapper.writeValueAsString(order))
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Order.class);
         }
    	 
    	 public Double totalPrice(Integer orderId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/orders/totalPrice/"  + orderId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Double.class);
         }
    	 
    	 public Boolean checkOrderExist(Integer orderId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/orderCheck/"  + orderId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Boolean.class);
         }
    	 
    	 public HashMap<Integer, Integer> getQuantity(Integer orderId) throws Exception {
    		 MockHttpServletResponse response = mockMvc.perform(get("/api/orders/quantity/"  + orderId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), new TypeReference<HashMap<Integer, Integer> >(){});
         }
    	 
    	 public List<Order> getOrdersFromInterval(DataOrderInterval dateInterval) throws Exception {
             String json = objectMapper.writeValueAsString(dateInterval);
             MockHttpServletResponse response =
                     mockMvc.perform(post("/api/orders/interval")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json)
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Order> >(){});
         }

    }
 }
