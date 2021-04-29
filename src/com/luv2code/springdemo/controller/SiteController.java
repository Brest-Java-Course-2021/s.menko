package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.CustomerService;
import com.luv2code.springdemo.service.ProductService;

@Controller
public class SiteController {

	// need to inject our customer service
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public String listCustomers(Model theModel) {
		
		// get customers from the service
		List<Category> theCategories = categoryService.getCategories();
		
		List<Product> theProducts = productService.getLatestProducts();
				
		// add the customers to the model
		theModel.addAttribute("categories",  theCategories);
		
		theModel.addAttribute("products",  theProducts);
		
		theModel.addAttribute("productService", productService);
		
		
		
		return "site/index";
	}
	
	
}










