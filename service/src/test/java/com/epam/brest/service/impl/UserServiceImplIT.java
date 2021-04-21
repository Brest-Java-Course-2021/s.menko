package com.epam.brest.service.impl;

import com.epam.brest.model.Category;
import com.epam.brest.model.User;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.UserService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({UserServiceImpl.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@Transactional
class UserServiceImplIT {

    @Autowired
    UserService userService;
    
    @Test
    public void checkEmailExistTest() {
        assertTrue(userService.checkEmailExist("admin@epam.com"));
        assertTrue(!userService.checkEmailExist("manager@epam.com"));
    }  
    @Test
    public void getUserByIdTest() {

         User expUser = userService.getUserById(1).get();
         Assertions.assertEquals(1, expUser.getId());
         Assertions.assertEquals("admin", expUser.getName());
         Assertions.assertEquals("admin", expUser.getRole());
         Assertions.assertEquals("admin@epam.com", expUser.getEmail());
    }
    
    @Test
    public void saveUserTest() {
    	
    	User user = new User("support","support@epam.com","123456","user");
    	userService.saveUser(user);
    	User expUser = userService.getUserById(user.getId()).get();

    	Assertions.assertEquals(user.getId(), expUser.getId());
        Assertions.assertEquals(user.getName(), expUser.getName());
        Assertions.assertEquals(user.getRole(), expUser.getRole());
        Assertions.assertEquals(user.getEmail(), expUser.getEmail());
        Assertions.assertEquals(user,expUser);
    }
    
    @Test
    public void checkUserDataTest() {
    	assertTrue(userService.checkUserData("admin@epam.com","123456")>0);
        assertTrue(userService.checkUserData("manager@epam.com","123456")==0);
    }
    
    @Test
    public void checkEmailWithDifferentIdExistTest() {
    	assertTrue(userService.checkEmailWithDifferentIdExist(2,"admin@epam.com"));
        assertTrue(!userService.checkEmailWithDifferentIdExist(1,"manager@epam.com"));
    }
    
    @Test
    public void checkUserExistTestTest() {
    	assertTrue(userService.checkUserExist(1));
        assertTrue(!userService.checkUserExist(999));
    }
    
    @Test
    public void getUserByIdExceptionalTest() {

        Optional<User> optionalUser = userService.getUserById(999);
        assertTrue(optionalUser.isEmpty());
    }
    
    @Test
    public void updateUserTest() {
    	
    	User user = new User(2,"ceo","ceo@epam.com","123456","admin");
    	userService.saveUser(user);
    	User expUser = userService.getUserById(user.getId()).get();

    	Assertions.assertEquals(user.getId(), expUser.getId());
        Assertions.assertEquals(user.getName(), expUser.getName());
        Assertions.assertEquals(user.getRole(), expUser.getRole());
        Assertions.assertEquals(user.getEmail(), expUser.getEmail());
        Assertions.assertEquals(user,expUser);
    }
}
