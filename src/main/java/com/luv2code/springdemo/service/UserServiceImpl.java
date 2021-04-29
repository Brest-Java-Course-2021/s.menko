package com.luv2code.springdemo.service;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.luv2code.springdemo.dao.UserDAO;
import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.CheckUserData;
import com.luv2code.springdemo.entity.User;

@Service
public class UserServiceImpl implements UserService {

	private RestTemplate restTemplate;
	private String crmRestUrl="http://localhost:8080/web-customer-tracker/api/users";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public UserServiceImpl(RestTemplate theRestTemplate) {
		 restTemplate=theRestTemplate;
	}
	
	@Override
	public boolean checkEmailExist(String email) {
		return !restTemplate.postForEntity(crmRestUrl+"/emailExist", email, Boolean.class).getBody();	
	}

	@Override
	public void saveUser(User user) {
		int userId = user.getId();
		if (userId == 0) {
			restTemplate.postForEntity(crmRestUrl, user, String.class);			
		} else {
			restTemplate.put(crmRestUrl, user);
		}
	}

	@Override
	public Optional<User> checkUserData(String email, String password) {
		
		RequestEntity<CheckUserData> request = RequestEntity
	            .post(URI.create(crmRestUrl+"/checkData"))
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(new CheckUserData(email,password));
		ResponseEntity<Optional<User>> response = restTemplate.exchange(
				crmRestUrl+"/checkData", 
	            HttpMethod.POST, 
	            request, 
	            new ParameterizedTypeReference<Optional<User>>() {}
	            );
		return response.getBody();
	}

	@Override
	public User getUserById(int id) {
		
		User theUser= 
				restTemplate.getForObject(crmRestUrl + "/" + id, 
						User.class);
		return theUser;
	}

	@Override
	public Optional<Integer> getIdFromSession() {
		return Optional.ofNullable((Integer)session.getAttribute("usersId"));
	}


}
