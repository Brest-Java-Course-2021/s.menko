package com.luv2code.springdemo.dao;

import java.util.List;
import java.util.Optional;

import com.luv2code.springdemo.entity.Category;

public interface CategoryDAO {

	public List<Category> getCategories();
	public void saveCategory(Category theCategory);
	public void deleteCategory(int id);
	public Optional<Category> getCategorytById(int id);
	public boolean productOfCategoryInOrderExist(int idCategory);
	public boolean checkCategoryExist(int id);
	
	
}
