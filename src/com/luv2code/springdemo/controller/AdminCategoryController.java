package com.luv2code.springdemo.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController extends AdminBase {
	
	@Autowired
	private CategoryService categoryService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String showCategoryList(Model theModel) throws IOException {
		
		Integer userId = checkLogged();
		
		if(userId!=null) {
		
			List<Category> theCategories = categoryService.getCategories();
			theModel.addAttribute("categories",  theCategories);
		}
		
		return "admin_category/index";
	}
	
	@GetMapping("/create")
	public String createCategory(Model theModel) {
		
		// delete the customer
		Category theCategory = new Category();
		
		theModel.addAttribute("category", theCategory);
		
		return "admin_category/createOrUpdate";
	}
	
	@PostMapping("/save")
	public String saveCategory(Model theModel, @Valid @ModelAttribute("category") Category theCategory, 
			BindingResult theBindingResult) {
		
		if (theBindingResult.hasErrors()) {
			System.out.println(theCategory.getProducts());
			theModel.addAttribute("category", theCategory);
		}
		else {
			categoryService.saveCategory(theCategory);
			return "redirect:/admin/category";
		}

		return "admin_category/createOrUpdate";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam("categoryId") int theId) {
		
		// delete the customer
		categoryService.deleteCategory(theId);
		
		return "redirect:/admin/category";
	}
	
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("categoryId") int theId,
									Model theModel) {
		
		// get the customer from our service
		Category theCategory = categoryService.getCategorytById(theId);	
		
		// set customer as a model attribute to pre-populate the form
		theModel.addAttribute("category", theCategory);
		
		//session.setAttribute("s", theCategory.getProducts());
		
		// send over to our form		
		return "admin_category/createOrUpdate";
	}
	

}
