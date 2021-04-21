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
@Import({AdminController.class,AdminCategoryController.class})
class AdminCategoryControllerIT {

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
    public void showAdminPageLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isFound())
        		.andExpect(view().name("redirect:/user/login"))
        		.andExpect(redirectedUrl("/user/login"));
        mockServer.verify();
    }
    
    @Test
    public void showAdminPageNotAdmin() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","user");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
        		.andExpect(view().name("admin/index"))
        		.andExpect(model().attribute("error", is("В доступе отказано!")));;
        mockServer.verify();
    }
    
    @Test
    public void showAdminPageSuccess() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
        		.andExpect(view().name("admin/index"))
        		.andExpect(model().attribute("error", nullValue()));;
        mockServer.verify();
    }
    
    @Test
    public void showAdminCategoryPageNotAdmin() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","user");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category").session(session)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin"))
    		.andExpect(redirectedUrl("/admin"));
        mockServer.verify();
    }
    
    @Test
    public void showAdminCategoryPage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	Category c1 = createCategory(1,"Блузки",2,1);
    	Category c2 = createCategory(1,"Шорты",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(Arrays.asList(c1,c2))));
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category").session(session)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_category/index"))
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
    public void showCreateCategoryPage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category/create").session(session)
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_category/createOrUpdate"))
    		 .andExpect(model().attribute("category", isA(Category.class)));

        mockServer.verify();
    }
    
    @Test
    public void saveCategoryProcessValidationError() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/category/save").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "0")
                .param("name", " ")
                .param("sortOrder", "2")
                .param("status", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_category/createOrUpdate"))
    		.andExpect(model().attribute("category", isA(Category.class)))
    		.andExpect(model().attribute("category", hasProperty("id", is(0))))
    		.andExpect(model().attribute("category", hasProperty("name", nullValue())))
    		.andExpect(model().attribute("category", hasProperty("sortOrder", is(2))))
    		.andExpect(model().attribute("category", hasProperty("status", is(1))));

        mockServer.verify();
    }

    @Test
    public void saveCategoryProcessSuccess() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/category/save").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "0")
                .param("name", "Блузки")
                .param("sortOrder", "2")
                .param("status", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin/category"))
    		.andExpect(redirectedUrl("/admin/category"))
    		.andExpect(model().attribute("category", isA(Category.class)))
    		.andExpect(model().attribute("category", hasProperty("id", is(0))))
    		.andExpect(model().attribute("category", hasProperty("name", is("Блузки"))))
    		.andExpect(model().attribute("category", hasProperty("sortOrder", is(2))))
    		.andExpect(model().attribute("category", hasProperty("status", is(1))));

        mockServer.verify();
    }
    
    @Test
    public void deleteCategoryInOrderError() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/CategoryInOrder/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(true)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category/delete").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("categoryId", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin/category"))
    		.andExpect(redirectedUrl("/admin/category"));
    	
    	assertTrue((Boolean)session.getAttribute("MessageCategory"));

        mockServer.verify();
    }
    
    @Test
    public void deleteCategory() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/CategoryInOrder/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(false)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories/1")))
        .andExpect(method(HttpMethod.DELETE))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category/delete").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("categoryId", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin/category"))
    		.andExpect(redirectedUrl("/admin/category"));
    	
    	assertNull(session.getAttribute("MessageCategory"));

        mockServer.verify();
    }
    
    @Test
    public void shouldOpenUpdatePage() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	Category c = createCategory(1,"Блузки",2,1);
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(c)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/category/update").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("categoryId", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isOk())
    		.andExpect(view().name("admin_category/createOrUpdate"))
    		.andExpect(model().attribute("category", isA(Category.class)))
    		.andExpect(model().attribute("category", hasProperty("id", is(c.getId()))))
    		.andExpect(model().attribute("category", hasProperty("name", is(c.getName()))))
    		.andExpect(model().attribute("category", hasProperty("sortOrder", is(c.getSortOrder()))))
    		.andExpect(model().attribute("category", hasProperty("status", is(c.getStatus()))));

        mockServer.verify();
    }
    
    @Test
    public void updateCategoryProcessSuccess() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/categories")))
        .andExpect(method(HttpMethod.PUT))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1)));
    	
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/category/save").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Блузки")
                .param("sortOrder", "2")
                .param("status", "1")
        ).andDo(MockMvcResultHandlers.print())
    		.andExpect(status().isFound())
    		.andExpect(view().name("redirect:/admin/category"))
    		.andExpect(redirectedUrl("/admin/category"))
    		.andExpect(model().attribute("category", isA(Category.class)))
    		.andExpect(model().attribute("category", hasProperty("id", is(1))))
    		.andExpect(model().attribute("category", hasProperty("name", is("Блузки"))))
    		.andExpect(model().attribute("category", hasProperty("sortOrder", is(2))))
    		.andExpect(model().attribute("category", hasProperty("status", is(1))));

        mockServer.verify();
    }
    
   
    	
    private Category createCategory(int id, String name,int sortOrder,int status) {
    	Category category = new Category(name,sortOrder, status);
    	category.setId(id);
        return category;
    }

}



