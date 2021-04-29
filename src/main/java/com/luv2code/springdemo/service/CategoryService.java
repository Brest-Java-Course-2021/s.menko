package com.luv2code.springdemo.service;

import java.util.List;

import com.luv2code.springdemo.entity.Category;

public interface CategoryService {
	
	public List<Category> getCategories();
	public void saveCategory(Category theCategory);
	public void deleteCategory(int id);
	public Category getCategorytById(int id);
	public boolean productOfCategoryInOrderExist(int idCategory);

}
