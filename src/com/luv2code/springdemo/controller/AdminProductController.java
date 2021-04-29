package com.luv2code.springdemo.controller;

import java.io.File;
import java.io.FileOutputStream;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.ProductService;
import com.luv2code.springdemo.service.UserService;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController extends AdminBase{
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	
	@GetMapping
	public String showProductList(Model theModel) throws IOException {
		
		Integer userId = checkLogged();
		
		if(userId!=null) {
		
			List<Product> productList = productService.getProdactList();
			theModel.addAttribute("allProducts", productList);
		}
		
		return "admin_product/index";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam("productId") int theId) {
		
		// delete the customer
		productService.deleteProduct(theId);
		
		return "redirect:/admin/product";
	}
	
	@GetMapping("/create")
	public String createProduct(Model theModel) {
		
		// delete the customer
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
		
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("product", theProduct);
			List<Category> theCategories = categoryService.getCategories();
			theModel.addAttribute("categories",  theCategories);
			System.out.println(theBindingResult);
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
		
		
		
		theModel.addAttribute("productService", productService);
		
		// get the customer from our service
		Product theProduct = productService.getProdactById(theId);	
		
		// set customer as a model attribute to pre-populate the form
		theModel.addAttribute("product", theProduct);
		
		List<Category> theCategories = categoryService.getCategories();
		theModel.addAttribute("categories",  theCategories);
		
		// send over to our form		
		return "admin_product/createOrUpdate";
	}
	

}
