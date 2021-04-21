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
@Import({SiteController.class,ProductController.class,CatalogController.class})
class SiteControllerIT {

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
    public void shouldReturnSitePage() throws Exception {
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
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/latest")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("site/index"))
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
                .andExpect(model().attribute("productService", isA(ProductService.class)))
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
    
    @Test
    public void shouldReturnProductPage() throws Exception {
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	Product p1 = createProduct(1,"Мужская футболка", 123.99, 1, 
    			"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image");
    	
    	
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    
    	
    	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/"+p1.getId())))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(p1)));
        	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/product/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("product/product-details"))
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
                .andExpect(model().attribute("product", hasProperty("id", is(p1.getId()))))
                .andExpect(model().attribute("product", hasProperty("name", is(p1.getName()))))
                .andExpect(model().attribute("product", hasProperty("price", is(p1.getPrice()))))
                .andExpect(model().attribute("product", hasProperty("availability", is(p1.getAvailability()))))
                .andExpect(model().attribute("product", hasProperty("description", is(p1.getDescription()))))
                .andExpect(model().attribute("productService", isA(ProductService.class)))
                .andExpect(model().attribute("tempId", is(1)));;
    	 mockServer.verify();
    }
    
    
    @Test
    public void shouldReturnCatalogPage() throws Exception {
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
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/latest")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/catalog")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("catalog/index"))
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
                .andExpect(model().attribute("productService", isA(ProductService.class)))
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
    
    @Test
    public void shouldReturnCategoryPage() throws Exception {
    	
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
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/category-"+p1.getCategory_id()+"/page-1")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/totalProducts/"+p1.getCategory_id())))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(2)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/catalog/category/3")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("catalog/index"))
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
                )))
                .andExpect(model().attribute("productService", isA(ProductService.class)))
                .andExpect(model().attribute("pagination", isA(Pagination.class)))
                .andExpect(model().attribute("pagination", hasProperty("total", is(2.0))))
                .andExpect(model().attribute("tempId", is(3)));
    	 mockServer.verify();
    }
    
    @Test
    public void shouldReturnListProductsOnPage() throws Exception {
    	
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
    	
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/category-"+p1.getCategory_id()+"/page-2")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(Arrays.asList(p1,p2))));
        	
        	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/products/totalProducts/"+p1.getCategory_id())))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(2)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/catalog/category/3/page-2")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("catalog/index"))
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
                )))
                .andExpect(model().attribute("productService", isA(ProductService.class)))
                .andExpect(model().attribute("pagination", isA(Pagination.class)))
                .andExpect(model().attribute("pagination", hasProperty("total", is(2.0))))
                .andExpect(model().attribute("tempId", is(3)));
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



