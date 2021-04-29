package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Pagination;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.ProductService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	
	@GetMapping
	public String getCatalog(Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
		
		List<Product> theProducts = productService.getLatestProducts();
				
		theModel.addAttribute("categories",  theCategories);
		
		theModel.addAttribute("products",  theProducts);
		theModel.addAttribute("productService", productService);
		
		return "catalog/index";
	}
	
	@GetMapping("/category/{id}")
	public String getCategory(@PathVariable("id") int id, Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
				
		theModel.addAttribute("categories",  theCategories);
		theModel.addAttribute("tempId",  id);
		
		List<Product> theProducts = productService.getProdactsListByCategory(id,1);
		
		int totalProductOfCategory =productService.getTotalProdactsInCategory(id);
		Pagination pagination = new Pagination(totalProductOfCategory,1,id);
		theModel.addAttribute("pagination", pagination);
		theModel.addAttribute("products",  theProducts);
		theModel.addAttribute("productService", productService);
		return "catalog/index";
	}
	
	@GetMapping("/category/{id}/page-{number}")
	public String getListProduct(@PathVariable("id") int id,@PathVariable("number") int number, Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
				
		theModel.addAttribute("categories",  theCategories);
		theModel.addAttribute("tempId",  id);
		
		List<Product> theProducts = productService.getProdactsListByCategory(id,number);
		theModel.addAttribute("products",  theProducts);
		int totalProductOfCategory =productService.getTotalProdactsInCategory(id);
		Pagination pagination = new Pagination(totalProductOfCategory,number,id);
		theModel.addAttribute("pagination", pagination);
		theModel.addAttribute("productService", productService);
		return "catalog/index";
	}
	
	
	
}
