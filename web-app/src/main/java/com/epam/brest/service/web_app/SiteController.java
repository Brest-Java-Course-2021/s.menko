package com.epam.brest.service.web_app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.epam.brest.model.Category;
import com.epam.brest.model.Product;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ProductService;
//
@Controller
public class SiteController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public String mainPage(Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
		List<Product> theProducts = productService.getLatestProducts();	
		theModel.addAttribute("categories",  theCategories);
		theModel.addAttribute("products",  theProducts);
		theModel.addAttribute("productService", productService);
		
		return "site/index";
	}
	
	
}










