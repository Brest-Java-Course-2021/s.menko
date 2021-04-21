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
@Import({CartController.class})
class CartControllerIT {

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
    public void countOfProductsFromCart() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	productsMap.put(3, 5);
    	session.setAttribute("products", productsMap);
    	
    	MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/cart/add/1").session(session))
    			  .andReturn();
    	
    	Assertions.assertEquals(result.getResponse().getContentAsString(), "("+13+")");
    	Assertions.assertEquals(session.getAttribute("countItrems"), 13);
    	assertTrue((Boolean)session.getAttribute("cartList"));
        mockServer.verify();
    }
    
    @Test
    public void showCartNoProducts() throws Exception {
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	MockHttpSession session = new MockHttpSession();
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cart").session(session)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/index"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )));
    	 mockServer.verify();

    }
    
    @Test
    public void showCart() throws Exception {
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/byIds")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	MockHttpSession session = new MockHttpSession();
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cart").session(session)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/index"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )))
                .andExpect(model().attribute("productsByIds", hasItem(
                        allOf(
                        		hasProperty("id", is(p1.getId())),
                                hasProperty("name", is(p1.getName())),
                                hasProperty("price", is(p1.getPrice())),
                                hasProperty("availability", is(p1.getAvailability())),
                                hasProperty("brand", is(p1.getBrand())),
                                hasProperty("description", is(p1.getDescription()))
                        )
                )))
                .andExpect(model().attribute("productsByIds", hasItem(
                        allOf(
                        		hasProperty("id", is(p1.getId())),
                                hasProperty("name", is(p1.getName())),
                                hasProperty("price", is(p1.getPrice())),
                                hasProperty("availability", is(p1.getAvailability())),
                                hasProperty("brand", is(p1.getBrand())),
                                hasProperty("description", is(p1.getDescription()))
                        )
                )))
                .andExpect(model().attribute("totalPrice", is(853.97)));
    	 mockServer.verify();

    }
    
    @Test
    public void removeFromCart() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/delete/1").session(session))
    			 .andExpect(status().isFound())
          		.andExpect(view().name("redirect:/cart"))
          		.andExpect(redirectedUrl("/cart"));;
    
    	Assertions.assertEquals(session.getAttribute("countItrems"), 4);
    	assertTrue((Boolean)session.getAttribute("cartList"));
        mockServer.verify();
    }
    
    @Test
    public void checkoutOrderWithLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/byIds")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/checkout").session(session)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/checkout"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )))
                .andExpect(model().attribute("totalPrice", is(853.97)))
                .andExpect(model().attribute("errorExist", is(true)))
                .andExpect(model().attribute("order", isA(Order.class)));
    
        mockServer.verify();
    }
    
    @Test
    public void checkoutOrderWithLogIn() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/byIds")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/checkout").session(session)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/checkout"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )))
                .andExpect(model().attribute("totalPrice", is(853.97)))
                .andExpect(model().attribute("errorExist", is(true)))
                .andExpect(model().attribute("order", isA(Order.class)))
                .andExpect(model().attribute("order", hasProperty("userName", is(user.getName()))));
    
        mockServer.verify();
    }
    
    @Test
    public void checkoutProcessValidationError() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/byIds")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/cart/checkout/process").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "am")
                .param("userPhone", "375.29245-47")
                .param("userComment", "Звонить после в 20:00")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/checkout"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )))
                .andExpect(model().attribute("errorExist", is(true)))
                .andExpect(model().attribute("totalPrice", is(853.97)))
                .andExpect(model().attribute("order", isA(Order.class)))
                .andExpect(model().attribute("order", hasProperty("userName", is("am"))))
        		.andExpect(model().attribute("order", hasProperty("userPhone", is("375.29245-47"))))
        		.andExpect(model().attribute("order", hasProperty("userComment", is("Звонить после в 20:00"))));
        mockServer.verify();
    }
    
    @Test
    public void checkoutProcessSuccess() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	HashMap<Integer, Integer> productsMap = new HashMap<>();
    	productsMap.put(1, 3);
    	productsMap.put(2, 4);
    	session.setAttribute("products", productsMap);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	Product p2 = createProduct(2,"Женская футболка", 120.5, 1, 
    			"Nike","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/byIds")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/orders")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/cart/checkout/process").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "admin")
                .param("userPhone", "37529245-47-88")
                .param("userComment", "Звонить после в 20:00")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("cart/checkout"))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("sortOrder", is(c1.getSortOrder())),
                                hasProperty("status", is(c1.getStatus()))
                        )
                )))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                        		hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("sortOrder", is(c2.getSortOrder())),
                                hasProperty("status", is(c2.getStatus()))
                        )
                )))
                .andExpect(model().attribute("errorExist", is(false)))
                .andExpect(model().attribute("totalPrice", is(853.97)))
                .andExpect(model().attribute("order", isA(Order.class)))
                .andExpect(model().attribute("order", hasProperty("userName", is("admin"))))
        		.andExpect(model().attribute("order", hasProperty("userPhone", is("37529245-47-88"))))
        		.andExpect(model().attribute("order", hasProperty("userComment", is("Звонить после в 20:00"))))
        		.andExpect(model().attribute("order", hasProperty("userId", is(1))));
    	
    	assertFalse((Boolean)session.getAttribute("cartList"));
    	assertNull(session.getAttribute("products"));
    	assertNull(session.getAttribute("countItrems"));
        mockServer.verify();
    }

    	
    private Category createCategory(int id, String name,int sortOrder,int status) {
    	Category category = new Category(name,sortOrder, status);
    	category.setId(id);
        return category;
    }
    private Product createProduct(int id, String name, double price, int availability, String brand,
    		String description, int category_id,String image) {
    	Product product =  new Product(name, price,availability, brand,
        		description,category_id,image);
		product.setId(id);
        return product;
    }
    
}



