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
@Import({CabinetController.class})
class CabinetControllerIT {

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
    public void showCabinetForLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cabinet").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isFound())
        		.andExpect(view().name("redirect:/user/login"))
        		.andExpect(redirectedUrl("/user/login"));
        mockServer.verify();
    }
    
    @Test
    public void showCabinetForLogIn() throws Exception {
    	
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cabinet").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
        		.andExpect(view().name("cabinet/index"))
        		.andExpect(model().attribute("user", isA(User.class)))
        		.andExpect(model().attribute("user", hasProperty("name", is(user.getName()))))
            	.andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))))
                .andExpect(model().attribute("user", hasProperty("password", is(user.getPassword()))))
                .andExpect(model().attribute("user", hasProperty("role", is(user.getRole()))));;
        mockServer.verify();
    }
    
    @Test
    public void showEditLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cabinet/edit").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isFound())
        		.andExpect(view().name("redirect:/user/login"))
        		.andExpect(redirectedUrl("/user/login"));
        mockServer.verify();
    }
    
    @Test
    public void showEdit() throws Exception {
    	
    	User user=new User(1,"admin","admin@epam.com","123456","admin");
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/1")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(user)));
    	
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/cabinet/edit").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isOk())
        		.andExpect(view().name("cabinet/edit"))
        		.andExpect(model().attribute("save", is(false)))
        		.andExpect(model().attribute("user", isA(User.class)))
        		.andExpect(model().attribute("user", hasProperty("id", is(user.getId()))))
        		.andExpect(model().attribute("user", hasProperty("name", is(user.getName()))))
            	.andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))))
                .andExpect(model().attribute("user", hasProperty("role", is(user.getRole()))));;
        mockServer.verify();
    }
    
    @Test
    public void editSaveLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	mockMvc.perform(
    			MockMvcRequestBuilders.post("/cabinet/edit/save").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isFound())
        		.andExpect(view().name("redirect:/user/login"))
        		.andExpect(redirectedUrl("/user/login"));
        mockServer.verify();
    }
    
    @Test
    public void editSaveValidationError() throws Exception {

    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/cabinet/edit/save").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("name", "ad")
                        .param("email", "admin@epam.com")
                        .param("password", "12")
        )
    		.andExpect(model().attribute("save", is(false)))
        	.andExpect(model().attribute("user", isA(User.class)))
        	.andExpect(model().attribute("user", hasProperty("id", is(1))))
        	.andExpect(model().attribute("user", hasProperty("name", is("ad"))))
        	.andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("12"))))
            .andExpect(status().isOk())
            .andExpect(view().name("cabinet/edit"));
    	
        mockServer.verify();
    }
    
    @Test
    public void editSaveSuccess() throws Exception {
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users")))
        .andExpect(method(HttpMethod.PUT))
        .andRespond(withStatus(HttpStatus.OK)
        		.contentType(MediaType.APPLICATION_JSON)
                .body("1"));

    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/cabinet/edit/save").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("name", "admin")
                        .param("email", "admin@epam.com")
                        .param("password", "123456")
        )
    		.andExpect(model().attribute("save", is(true)))
        	.andExpect(model().attribute("user", isA(User.class)))
        	.andExpect(model().attribute("user", hasProperty("id", is(1))))
        	.andExpect(model().attribute("user", hasProperty("name", is("admin"))))
        	.andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123456"))))
            .andExpect(status().isOk())
            .andExpect(view().name("cabinet/edit"));
    	
        mockServer.verify();
    }
    

    
}



