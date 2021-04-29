package com.luv2code.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.dao.CategoryDAO;
import com.luv2code.springdemo.entity.Category;

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
	public Category getCategorytById(int id) {
		return categoryDAO.getCategorytById(id);
	}

	@Override
	@Transactional
	public boolean productOfCategoryInOrderExist(int idCategory) {
		return categoryDAO.productOfCategoryInOrderExist(idCategory);
	}



}
