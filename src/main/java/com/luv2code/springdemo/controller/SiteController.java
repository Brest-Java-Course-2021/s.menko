package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.ProductService;

@Controller
public class SiteController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public String listCustomers(Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
		List<Product> theProducts = productService.getLatestProducts();	
		theModel.addAttribute("categories",  theCategories);
		theModel.addAttribute("products",  theProducts);
		theModel.addAttribute("productService", productService);
		
		return "site/index";
	}
	
	
}










