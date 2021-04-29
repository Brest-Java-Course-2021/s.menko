package com.luv2code.springdemo.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.ProductService;


@Controller
@RequestMapping("/admin/product")
public class AdminProductController extends AdminBase{
	
	@Autowired
	private ProductService productService;
	
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
	public String showProductList(Model theModel) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		List<Product> productList = productService.getProdactList();
		theModel.addAttribute("allProducts", productList);
		theModel.addAttribute("MessageProduct",session.getAttribute("MessageProduct"));
		session.removeAttribute("MessageProduct");
		return "admin_product/index";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(Model theModel,@RequestParam("productId") int theId) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		if(!productService.productInOrderExist(theId))
			productService.deleteProduct(theId);
		else
			session.setAttribute("MessageProduct",true);
		
		return "redirect:/admin/product";
	}
	
	@GetMapping("/create")
	public String createProduct(Model theModel) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		Product theProduct = new Product();
		
		theModel.addAttribute("product", theProduct);
		
		theModel.addAttribute("productService", productService);
		
		List<Category> theCategories = categoryService.getCategories();
		theModel.addAttribute("categories",  theCategories);
		
		return "admin_product/createOrUpdate";
	}
	
	@PostMapping("/save")
	public String saveProduct(Model theModel, @Valid @ModelAttribute("product") Product theProduct, 
			BindingResult theBindingResult,@RequestParam("image") MultipartFile img)  throws IOException {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("product", theProduct);
			List<Category> theCategories = categoryService.getCategories();
			theModel.addAttribute("categories",  theCategories);
		}
		else {
			int id = productService.saveProduct(theProduct);
			productService.saveImage(img, id);
			return "redirect:/admin/product";
		}

		return "admin_product/createOrUpdate";
	}
	
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("productId") int theId,
									Model theModel)  {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		theModel.addAttribute("productService", productService);
		Product theProduct = productService.getProdactById(theId);	
		theModel.addAttribute("product", theProduct);
		List<Category> theCategories = categoryService.getCategories();
		theModel.addAttribute("categories",  theCategories);		
		return "admin_product/createOrUpdate";
	}
	

}
