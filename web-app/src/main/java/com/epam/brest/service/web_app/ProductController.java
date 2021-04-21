package com.epam.brest.service.web_app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.brest.model.Category;
import com.epam.brest.model.Product;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ProductService;

//
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
		theModel.addAttribute("categories",  theCategories);
		Product tempProduct = productService.getProdactById(id).get(); 
		theModel.addAttribute("product",tempProduct);
		theModel.addAttribute("productService", productService);
		return "product/product-details";
	}
	
	
}










