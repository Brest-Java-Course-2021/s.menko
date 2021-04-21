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
@Import({UserController.class})
class UserControllerIT {

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
    public void shouldshowLoginForm() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/login")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("user/login"))
                .andExpect(model().attribute("user", isA(User.class)));
        mockServer.verify();
    }
    
    @Test
    public void shouldLogOut() throws Exception {
    	
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute("usersId", 1);
    	mockMvc.perform(
                MockMvcRequestBuilders.get("/user/logout").session(session)
        ).andDo(MockMvcResultHandlers.print())
        		.andExpect(status().isFound())
        		.andExpect(view().name("redirect:/"))
        		.andExpect(redirectedUrl("/"));
    	assertNull(session.getAttribute("usersId"));
        mockServer.verify();
    }
    
    
    @Test
    public void shouldRegisterForm() throws Exception {

            mockMvc.perform(
                    MockMvcRequestBuilders.get("/user/register")
            ).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("user/register"))
                    .andExpect(model().attribute("user", isA(User.class)))
                    .andExpect(model().attribute("register", is(false)))
                    .andExpect(model().attribute("notEmailExist", is(true)));
            mockServer.verify();
        
    }
    
    @Test
    public void shouldprocessFormRegisterSuchEmailExist() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/existenceOfEmail")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(true))
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "admin")
                        .param("email", "admin@epam.com")
                        .param("password", "123456")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        	.andExpect(view().name("user/register"))
        	.andExpect(model().attribute("user", isA(User.class)))
            .andExpect(model().attribute("user", hasProperty("name", is("admin"))))
            .andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123456"))))
        	.andExpect(model().attribute("register", is(false)))
        	.andExpect(model().attribute("notEmailExist", is(false)));

        mockServer.verify();
    }
    
    @Test
    public void shouldprocessFormRegisterIncorrectData() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/existenceOfEmail")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(false))
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "ad")
                        .param("email", "admin@")
                        .param("password", "123")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        	.andExpect(view().name("user/register"))
        	.andExpect(model().attribute("user", isA(User.class)))
            .andExpect(model().attribute("user", hasProperty("name", is("ad"))))
            .andExpect(model().attribute("user", hasProperty("email", is("admin@"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123"))))
        	.andExpect(model().attribute("register", is(false)))
        	.andExpect(model().attribute("notEmailExist", is(true)));

        mockServer.verify();
    }
    
    @Test
    public void shouldprocessFormRegister() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/existenceOfEmail")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(false))
                );
      
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processForm")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "admin")
                        .param("email", "admin@epam.com")
                        .param("password", "123456")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        	.andExpect(view().name("user/register"))
        	.andExpect(model().attribute("user", isA(User.class)))
        	.andExpect(model().attribute("user", hasProperty("name", is("admin"))))
            .andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123456"))))
        	.andExpect(model().attribute("register", is(true)));


        mockServer.verify();
    }
    
    @Test
    public void shouldprocessLoginIncorrectData() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processLogin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "admin")
                        .param("password", "123")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        	.andExpect(view().name("user/login"))
        	.andExpect(model().attribute("user", isA(User.class)))
            .andExpect(model().attribute("user", hasProperty("email", is("admin"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123"))))
        	.andExpect(model().attribute("error", is("No such user exists!")));

        mockServer.verify();
    }
    
    @Test
    public void shouldprocessLoginNoSuchUser() throws Exception {
        
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/dataCheck")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(0))
        );
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processLogin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "admin@epam.com")
                        .param("password", "123456")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        	.andExpect(view().name("user/login"))
        	.andExpect(model().attribute("user", isA(User.class)))
        	.andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123456"))))
        	.andExpect(model().attribute("error", is("No such user exists!")));

        mockServer.verify();
    }
    
    @Test
    public void shouldprocessLoginSuccess() throws Exception {
        
    	MockHttpSession session = new MockHttpSession();
    	
    	mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL+"/users/dataCheck")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(1))
        );
    	
    	mockMvc.perform(
                MockMvcRequestBuilders.post("/user/processLogin").session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "admin@epam.com")
                        .param("password", "123456")
        )
        	.andExpect(model().attribute("user", isA(User.class)))
        	.andExpect(model().attribute("user", hasProperty("email", is("admin@epam.com"))))
            .andExpect(model().attribute("user", hasProperty("password", is("123456"))))
            .andExpect(status().isFound())
    		.andExpect(view().name("redirect:"+"/cabinet"))
    		.andExpect(redirectedUrl("/cabinet"));
    	assertNotNull(session.getAttribute("usersId"));
    	
        mockServer.verify();
    }
    
}



