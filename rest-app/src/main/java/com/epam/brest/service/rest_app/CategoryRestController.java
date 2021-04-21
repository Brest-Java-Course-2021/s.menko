package com.epam.brest.service.rest_app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.impl.CategoryServiceImpl;
import com.epam.brest.service.rest_app.exception.NotFoundException;



@RestController
@RequestMapping("/api")
public class CategoryRestController {

	@Autowired
	private CategoryService categoryService;
	//
	@GetMapping("/categories")
	public List<Category> getCategories() {
		
		return categoryService.getCategories();
		
	}
	//
	@GetMapping("/categories/{categoryId}")
	public Category getCategorytById(@PathVariable int categoryId) {
		
		Category theCategory = categoryService.getCategoryById(categoryId).orElseThrow(() -> new NotFoundException("Category id not found - " + categoryId));
		
		return theCategory;
	}
	//
	@PostMapping("/categories")
	public Category saveCategory(@RequestBody Category theCategory) {
		
		theCategory.setId(0);
		categoryService.saveCategory(theCategory);
		
		return theCategory;
	}

	//
	@PutMapping("/categories")
	public Category updateCategory(@RequestBody Category theCategory) {
		
		if(!categoryService.checkCategoryExist(theCategory.getId()))
			throw new NotFoundException("Category id not found - " + theCategory.getId());
		categoryService.saveCategory(theCategory);
		
		return theCategory;
		
	}
	
	//
	@DeleteMapping("/categories/{categoryId}")
	public String deleteCategory(@PathVariable int categoryId) {

		if(!categoryService.checkCategoryExist(categoryId))
			throw new NotFoundException("Category id not found - " + categoryId);
		categoryService.deleteCategory(categoryId);
		
		return "Deleted category id - " + categoryId;
	}
	
	@GetMapping("/CategoryInOrder/{categoryId}")
	public boolean productOfCategoryInOrderExist(@PathVariable int categoryId) {
		if(!categoryService.checkCategoryExist(categoryId))
			throw new NotFoundException("Category id not found - " + categoryId);
		return categoryService.productOfCategoryInOrderExist(categoryId);
	}
//
	@GetMapping("/categoryCheck/{categoryId}")
	public boolean checkCategoryExist(@PathVariable int categoryId) {
		
		return categoryService.checkCategoryExist(categoryId);
			
	}
	
}


















