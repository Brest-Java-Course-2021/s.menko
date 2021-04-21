package com.epam.brest.service.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService {

	
	private RestTemplate restTemplate;

	private String crmRestUrl="http://localhost:8080/api";

	
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
	public Optional<Category> getCategoryById(int id) {
		
		Category theCategory = 
				restTemplate.getForObject(crmRestUrl+"/categories" + "/" + id, 
						Category.class);
		
		return Optional.ofNullable(theCategory);
	}

	@Override
	public boolean productOfCategoryInOrderExist(int idCategory) {
		
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/CategoryInOrder/" + idCategory, 
						Boolean.class);
		
		return result;
	}

	@Override
	public boolean checkCategoryExist(int id) {
		
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/categoryCheck/" + id, 
						Boolean.class);
		
		return result;
	}


}
