package com.luv2code.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.luv2code.springdemo.dao.CategoryDAO;
import com.luv2code.springdemo.entity.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	
	private RestTemplate restTemplate;

	private String crmRestUrl="http://localhost:8080/web-customer-tracker/api";

	
	@Autowired
	public CategoryServiceImpl(RestTemplate theRestTemplate) {
		 restTemplate=theRestTemplate;
	}
	
	@Override
	public List<Category> getCategories() {
		
		ResponseEntity<List<Category>> responseEntity = 
				restTemplate.exchange(crmRestUrl+"/categories", HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Category>>() {});
		List<Category> categories = responseEntity.getBody();

		return categories;
	}

	@Override
	public void saveCategory(Category theCategory) {
		
		int categoryId = theCategory.getId();

		if (categoryId == 0) {
			restTemplate.postForEntity(crmRestUrl+"/categories", theCategory, String.class);			
		
		} else {
			restTemplate.put(crmRestUrl+"/categories", theCategory);
		}
		
	}

	@Override
	public void deleteCategory(int id) {
		restTemplate.delete(crmRestUrl +"/categories"+"/" + id);
		
	}

	@Override
	public Category getCategorytById(int id) {
		
		Category theCategory = 
				restTemplate.getForObject(crmRestUrl+"/categories" + "/" + id, 
						Category.class);
		
		return theCategory;
	}

	@Override
	public boolean productOfCategoryInOrderExist(int idCategory) {
		
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/CategoryInOrderExist/" + idCategory, 
						Boolean.class);
		
		return result;
	}



}
