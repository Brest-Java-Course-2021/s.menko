package com.epam.brest.service.rest_app;

import com.epam.brest.dao.hiberbate.CategoryDAOImpl;
import com.epam.brest.model.Category;
import com.epam.brest.model.CheckUserData;
import com.epam.brest.model.User;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class UserControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIT.class);

    @Autowired
    private UserRestController userController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockUserService userService = new  MockUserService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(userController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(restExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void checkEmailExistTest() throws Exception {
        assertTrue(userService.checkEmailExist("admin@epam.com"));
        assertTrue(!userService.checkEmailExist("manager@epam.com"));
   
    } 
    
    @Test
    public void checkUserDataTest() throws Exception {
    	assertTrue(userService.checkUserData("admin@epam.com","123456")>0);
        assertTrue(userService.checkUserData("manager@epam.com","123456")==0);
    }
    
    @Test
    public void checkEmailWithDifferentIdExistTest() throws Exception {
    	assertTrue(userService.checkEmailWithDifferentIdExist(2,"admin@epam.com"));
        assertTrue(!userService.checkEmailWithDifferentIdExist(1,"manager@epam.com"));
    }
    
    @Test
    public void checkUserExistTest() throws Exception {
    	assertTrue(userService.checkUserExist(1));
        assertTrue(!userService.checkUserExist(999));
    }
    
    @Test
    public void getUserByIdTest() throws Exception {

         User expUser = userService.getUserById(1).get();
         Assertions.assertEquals(1, expUser.getId());
         Assertions.assertEquals("admin", expUser.getName());
         Assertions.assertEquals("admin", expUser.getRole());
         Assertions.assertEquals("admin@epam.com", expUser.getEmail());
    }
    
    @Test
    public void shouldReturnNotFoundOnMissedUser() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/users/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void saveUserTest() throws Exception {
    	
    	User user = new User("support","support@epam.com","123456","user");
    	Integer id = userService.saveUser(user);
    	User expUser = userService.getUserById(id).get();

    	Assertions.assertEquals(id, expUser.getId());
        Assertions.assertEquals(user.getName(), expUser.getName());
        Assertions.assertEquals(user.getRole(), expUser.getRole());
        Assertions.assertEquals(user.getEmail(), expUser.getEmail());
    }
    
    @Test
    public void shouldReturnSuchEmailExistOnUser() throws Exception {

    	User user = new User("support","admin@epam.com","123456","user");
    	String json = objectMapper.writeValueAsString(user);
        MockHttpServletResponse response =
                mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                        .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void updateUserTest() throws Exception {
    	
    	User user = new User(2,"analyst","analyst@epam.com","123456","admin");
    	userService.updateUser(user);
    	User expUser = userService.getUserById(user.getId()).get();

    	Assertions.assertEquals(user.getId(), expUser.getId());
        Assertions.assertEquals(user.getName(), expUser.getName());
        Assertions.assertEquals(user.getRole(), expUser.getRole());
        Assertions.assertEquals(user.getEmail(), expUser.getEmail());
    }
    
    @Test
    public void updateUserNotFoundTest() throws Exception {
    	
    	User user = new User(999,"analyst","analyst@epam.com","123456","admin");
    	
    	MockHttpServletResponse response = mockMvc.perform(put("/api/users/")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(user))
                 .accept(MediaType.APPLICATION_JSON)
         ).andExpect(status().isNotFound())
                 .andReturn().getResponse();
    	 assertNotNull(response);
    	
    }
    
    @Test
    public void updateUserSuchEmailExistTest() throws Exception {
    	
    	User user = new User(2,"admin","admin@epam.com","123456","admin");
    	
    	MockHttpServletResponse response = mockMvc.perform(put("/api/users/")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(user))
                 .accept(MediaType.APPLICATION_JSON)
         ).andExpect(status().isUnprocessableEntity())
                 .andReturn().getResponse();
    	 assertNotNull(response);
    	
    }
    
    
    ///////////////////////////////////////////////////////////
    private class MockUserService {
        public Boolean checkEmailExist(String email) throws Exception {
        	 String json = objectMapper.writeValueAsString(email);
            MockHttpServletResponse response =
                    mockMvc.perform(post("/api/users/existenceOfEmail")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Boolean.class);
        }
        
        public Integer checkUserData(String email,String password) throws Exception {

        	CheckUserData data = new  CheckUserData(email,password);
        	String json = objectMapper.writeValueAsString(data);
       	 MockHttpServletResponse response =
                   mockMvc.perform(post("/api/users/dataCheck")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(json)
                           .accept(MediaType.APPLICATION_JSON)
                   ).andExpect(status().isOk())
                           .andReturn().getResponse();
           return objectMapper.readValue(response.getContentAsString(), Integer.class);
       }
        
        public Boolean checkEmailWithDifferentIdExist(int userId, String email) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
            		MockMvcRequestBuilders.get("/api/checkEmailWithId/"+userId+"/"+email+"/")
            		.contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Boolean.class);
        }
        
        public Boolean checkUserExist(int userId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
            		MockMvcRequestBuilders.get("/api/checkUser/"+userId)
            		.contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Boolean.class);
        }
        
        public Optional<User> getUserById(Integer userId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get("/api/users/"  + userId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), User.class));
        }
        
        public Integer saveUser(User user) throws Exception {
            String json = objectMapper.writeValueAsString(user);
            MockHttpServletResponse response =
                    mockMvc.perform(post("/api/users/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
        
        public Integer  updateUser(User user) throws Exception {
            MockHttpServletResponse response =
                    mockMvc.perform(put("/api/users/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
        
    }
}
