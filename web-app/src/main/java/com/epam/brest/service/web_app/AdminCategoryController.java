package com.epam.brest.service.web_app;


import java.util.List;

import javax.servlet.http.HttpSession;
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

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;


@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController extends AdminBase {
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private HttpSession session;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String showCategoryList(Model theModel){
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
			List<Category> theCategories = categoryService.getCategories();
			theModel.addAttribute("categories",  theCategories);
			theModel.addAttribute("MessageCategory",session.getAttribute("MessageCategory"));
			session.removeAttribute("MessageCategory");
		
		return "admin_category/index";
	}
	
	@GetMapping("/create")
	public String createCategory(Model theModel) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		Category theCategory = new Category();
		
		theModel.addAttribute("category", theCategory);
		
		return "admin_category/createOrUpdate";
	}
	
	@PostMapping("/save")
	public String saveCategory(Model theModel, @Valid @ModelAttribute("category") Category theCategory, 
			BindingResult theBindingResult) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("category", theCategory);
		}
		else {
			categoryService.saveCategory(theCategory);
			return "redirect:/admin/category";
		}

		return "admin_category/createOrUpdate";
	}
	
	@GetMapping("/delete")
	public String deleteCategory(@RequestParam("categoryId") int theId) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		if(!categoryService.productOfCategoryInOrderExist(theId))
			categoryService.deleteCategory(theId);
		else
			session.setAttribute("MessageCategory",true);
		
		
		return "redirect:/admin/category";
	}
	
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("categoryId") int theId,
									Model theModel) {

		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		Category theCategory = categoryService.getCategoryById(theId).get();	
		
		theModel.addAttribute("category", theCategory);
				
		return "admin_category/createOrUpdate";
	}
	

}
