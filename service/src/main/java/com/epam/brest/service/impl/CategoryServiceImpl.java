package com.epam.brest.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.brest.dao.CategoryDAO;
import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	@Transactional
	public List<Category> getCategories() {
		return categoryDAO.getCategories();
	}
	
	@Override
	@Transactional
	public void saveCategory(Category theCategory) {
		 categoryDAO.saveCategory(theCategory);
		
	}
	
	@Override
	@Transactional
	public void deleteCategory(int id) {
		categoryDAO.deleteCategory(id);
		
	}
	
	@Override
	@Transactional
	public Optional<Category> getCategoryById(int id) {
		return categoryDAO.getCategoryById(id);
	}

	@Override
	@Transactional
	public boolean productOfCategoryInOrderExist(int idCategory) {
		return categoryDAO.productOfCategoryInOrderExist(idCategory);
	}

	@Override
	@Transactional
	public boolean checkCategoryExist(int id) {
		return categoryDAO.checkCategoryExist(id);
	}



}
