package com.epam.brest.service.web_app;

import com.epam.brest.model.Category;
import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.model.Pagination;
import com.epam.brest.model.Product;
import com.epam.brest.model.User;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.OrderService;
import com.epam.brest.service.ProductService;
import com.epam.brest.service.rest.CategoryServiceImpl;
import com.epam.brest.service.rest.ProductServiceImpl;
import com.epam.brest.service.web_app.config.ApplicationConfig;
import com.epam.brest.service.web_app.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles({ "test" })
@Import({AdminOrderController.class})
class AdminOrderControllerIT {

    private static final String URL = "http://localhost:8080/api";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void showAdminOrderPage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");

    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
        Order o1=createOrder(1,"Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
        Order o2=createOrder(2,"Ivan Vasiliev","375295-33-66-09","Звонить в будние дни после 20:00");
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(o1,o2))));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/order").session(session)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_order/index"))
    		 .andExpect(model().attribute("orders", hasItem(
                     allOf(
                             hasProperty("id", is(o1.getId())),
                             hasProperty("userName", is(o1.getUserName())),
                             hasProperty("userPhone", is(o1.getUserPhone())),
                             hasProperty("userComment", is(o1.getUserComment()))
                     )
             )))
             .andExpect(model().attribute("orders", hasItem(
                     allOf(
                    		 hasProperty("id", is(o2.getId())),
                             hasProperty("userName", is(o2.getUserName())),
                             hasProperty("userPhone", is(o2.getUserPhone())),
                             hasProperty("userComment", is(o2.getUserComment()))
                     )
             )))
             .andExpect(model().attribute("dataFormat", isA(DateTimeFormatter.class)))
             .andExpect(model().attribute("dataInterval", isA(DataOrderInterval.class)))
             .andExpect(model().attribute("dataValidation", is(true)));

        mockServer.verify();
    }
    
    @Test
    public void showAdminOrderFromIntervalPage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");

    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
        Order o1=createOrder(1,"Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
        Order o2=createOrder(2,"Ivan Vasiliev","375295-33-66-09","Звонить в будние дни после 20:00");
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/interval")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(o1,o2))));
    	
        String startDate = "2021-04-06T10:15";
        String endDate = "2025-03-27T10:15";	
        	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/order").session(session)
                .param("startData",startDate)
                .param("endData",endDate)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_order/index"))
    		 .andExpect(model().attribute("orders", hasItem(
                     allOf(
                             hasProperty("id", is(o1.getId())),
                             hasProperty("userName", is(o1.getUserName())),
                             hasProperty("userPhone", is(o1.getUserPhone())),
                             hasProperty("userComment", is(o1.getUserComment()))
                     )
             )))
             .andExpect(model().attribute("orders", hasItem(
                     allOf(
                    		 hasProperty("id", is(o2.getId())),
                             hasProperty("userName", is(o2.getUserName())),
                             hasProperty("userPhone", is(o2.getUserPhone())),
                             hasProperty("userComment", is(o2.getUserComment()))
                     )
             )))
             .andExpect(model().attribute("dataFormat", isA(DateTimeFormatter.class)))
             .andExpect(model().attribute("dataValidation", is(true)));

        mockServer.verify();
    }
    
    @Test
    public void showAdminOrderFromIntervalValidationError() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");

    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    
    	
    	  String startDate = "2025-04-06T10:15";
          String endDate = "2021-03-27T10:15";	
        	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/order").session(session)
                .param("startData",startDate)
                .param("endData",endDate)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_order/index"))
            .andExpect(model().attribute("dataFormat", isA(DateTimeFormatter.class)))
            .andExpect(model().attribute("dataValidation", is(false)));

        mockServer.verify();
    }
    
    @Test
    public void deleteOrder() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/1")))
        .andExpect(method(HttpMethod.DELETE))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/order/delete").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("orderId", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin/order"))
    		.andExpect(redirectedUrl("/admin/order"));

        mockServer.verify();
    }
    
    @Test
    public void showOrderForUpdatePage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");

    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	Order o=createOrder(1,"Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
    
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/"+o.getId())))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(o)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/order/update").session(session)
                .param("orderId", "1")	
    			).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_order/update"))
    		.andExpect(model().attribute("order", hasProperty("id", is(o.getId()))))
    		.andExpect(model().attribute("order", hasProperty("userName", is(o.getUserName()))))
    		.andExpect(model().attribute("order", hasProperty("userPhone", is(o.getUserPhone()))))
    		.andExpect(model().attribute("order", hasProperty("userComment", is(o.getUserComment()))));

        mockServer.verify();
    }
    
    @Test
    public void updateOrderProcessValidationError() throws Exception {


    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/order/save").session(session)
                .param("id", "1")
                .param("userName", "ad")
                .param("userPhone", "123.323.4"))
        .andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_order/update"))
    		.andExpect(model().attribute("order", hasProperty("id", is(1))))
    		.andExpect(model().attribute("order", hasProperty("userName", is("ad"))))
    		.andExpect(model().attribute("order", hasProperty("userPhone", is("123.323.4"))));

        mockServer.verify();
    }
    
    @Test
    public void updateOrderProcessSuccess() throws Exception {


    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	Order o=createOrder(1,"Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders")))
        .andExpect(method(HttpMethod.PUT))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/order/save").session(session)
                .param("id", "1")
                .param("userName", o.getUserName())
                .param("userPhone",o.getUserPhone())
                .param("userComment",o.getUserComment()))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isFound())
		.andExpect(view().name("redirect:/admin/order"))
		.andExpect(redirectedUrl("/admin/order"));

        mockServer.verify();
    }
    
    @Test
    public void showOrderPage() throws Exception {
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	Order o=createOrder(1,"Dmitry Lazovsky","375295-32-56-89","Скажите, когда будет выполнен заказ?");
        
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/"+o.getId())))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(o)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/totalPrice/"+o.getId())))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(853.97)));
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/ProdactsFromOrder/"+o.getId())))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders/quantity/"+o.getId())))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(productsMap)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/order/view").session(session)
                .param("orderId", "1"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
		.andExpect(view().name("admin_order/view"))
		.andExpect(model().attribute("dataFormat", isA(DateTimeFormatter.class)))
		.andExpect(model().attribute("orderService", isA(OrderService.class)))
		.andExpect(model().attribute("totalPrice", is(853.97)))
		.andExpect(model().attribute("order", hasProperty("id", is(o.getId()))))
 		.andExpect(model().attribute("order", hasProperty("userName", is(o.getUserName()))))
 		.andExpect(model().attribute("order", hasProperty("userPhone", is(o.getUserPhone()))))
 		.andExpect(model().attribute("order", hasProperty("userComment", is(o.getUserComment()))))
 		.andExpect(model().attribute("products", hasItem(
                allOf(
               		 hasProperty("id", is(p1.getId())),
                        hasProperty("name", is(p1.getName())),
                        hasProperty("price", is(p1.getPrice())),
                        hasProperty("availability", is(p1.getAvailability())),
                        hasProperty("brand", is(p1.getBrand())),
                        hasProperty("description", is(p1.getDescription()))
                )
        )))
        .andExpect(model().attribute("products", hasItem(
                allOf(
               		 hasProperty("id", is(p2.getId())),
                        hasProperty("name", is(p2.getName())),
                        hasProperty("price", is(p2.getPrice())),
                        hasProperty("availability", is(p2.getAvailability())),
                        hasProperty("brand", is(p2.getBrand())),
                        hasProperty("description", is(p2.getDescription()))
                )
        )));
    	
        mockServer.verify();
    }
    private Product createProduct(int id, String name, double price, int availability, String brand,
    		String description, int category_id,String image) {
    	Product product =  new Product(name, price,availability, brand,
        		description,category_id,image);
		product.setId(id);
        return product;
    }
    
    private Order createOrder(int id, String userName,String userPhone, String userComment) {
    	Order order =  new Order(id,userName,userPhone,userComment);
        return order;
    }
    
}



