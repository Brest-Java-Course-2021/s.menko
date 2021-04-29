package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/{id}")
	public String listCustomers(@PathVariable("id") int id,Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
		
		theModel.addAttribute("tempId",  id);
		// add the customers to the model
		theModel.addAttribute("categories",  theCategories);
		
		Product tempProduct = productService.getProdactById(id); 
		
		theModel.addAttribute("product",tempProduct);
		theModel.addAttribute("productService", productService);
		return "product/product-details";
	}
	
	
}










