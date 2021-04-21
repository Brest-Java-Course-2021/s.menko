package com.epam.brest.service.rest;


import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.brest.model.CheckUserData;
import com.epam.brest.model.User;
import com.epam.brest.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	private RestTemplate restTemplate;
	private String crmRestUrl="http://localhost:8080/api/users";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public UserServiceImpl(RestTemplate theRestTemplate) {
		 restTemplate=theRestTemplate;
	}
	
	@Override
	public boolean checkEmailExist(String email) {
		return !restTemplate.postForEntity(crmRestUrl+"/existenceOfEmail", email, Boolean.class).getBody();	
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
	public Integer checkUserData(String email, String password) {
		
		RequestEntity<CheckUserData> request = RequestEntity
	            .post(URI.create(crmRestUrl+"/dataCheck"))
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(new CheckUserData(email,password));
		ResponseEntity<Integer> response = restTemplate.exchange(
				crmRestUrl+"/dataCheck", 
	            HttpMethod.POST, 
	            request, 
	            Integer.class
	            );
		return response.getBody();
	}

	@Override
	public Optional<User> getUserById(int id) {
		
		User theUser= 
				restTemplate.getForObject(crmRestUrl + "/" + id, 
						User.class);
		return Optional.ofNullable(theUser);
	}

	@Override
	public Optional<Integer> getIdFromSession() {
		return Optional.ofNullable((Integer)session.getAttribute("usersId"));
	}

	@Override
	public boolean checkEmailWithDifferentIdExist(int id, String email) {
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/checkEmailWithId/" + id+"/"+email,Boolean.class);
		return result;
	}

	@Override
	public boolean checkUserExist(int id) {
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/checkUser/" + id,Boolean.class);
		return result;
	}
}
