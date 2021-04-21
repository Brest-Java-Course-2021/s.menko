package com.epam.brest.dao;

import java.util.List;
import java.util.Optional;

import com.epam.brest.model.Category;

public interface CategoryDAO {

	/**
     * Accesses the database and selects all categories
     * @return list of all categories
     */
	public List<Category> getCategories();
	/**
     * Accesses the database and saves the passed category
     * @return void
     */
	public void saveCategory(Category theCategory);
	/**
     * Accesses the database and delete the category by the passed id
     * @return void
     */
	public void deleteCategory(int id);
	/**
     * Accesses the database and return the category by the passed id
     * @return the resulting category or null wrapped in an Optional
     */
	public Optional<Category> getCategoryById(int id);
	/**
     * Accesses the database and checks if a product 
     * from this category is in the order
     * @return boolean true if the product is from a category in the order, false otherwise
     */
	public boolean productOfCategoryInOrderExist(int idCategory);
	/**
     * Accesses the database and checks if the given category exists
     * @return boolean true if the category exists, false otherwise
     */
	public boolean checkCategoryExist(int id);
	
	
}
