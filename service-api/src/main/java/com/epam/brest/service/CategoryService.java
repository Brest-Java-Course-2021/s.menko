package com.epam.brest.service;

import java.util.List;
import java.util.Optional;

import com.epam.brest.model.Category;

/**
 * This interface is implemented by two applications 
 * Rest App and Web App and calls the CategoryDAO methods
 */
public interface CategoryService {
	
	public List<Category> getCategories();
	public void saveCategory(Category theCategory);
	public void deleteCategory(int id);
	public Optional<Category> getCategoryById(int id);
	public boolean productOfCategoryInOrderExist(int idCategory);
	public boolean checkCategoryExist(int id);

}
