package com.luv2code.springdemo.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luv2code.springdemo.entity.Category;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.CartService;
import com.luv2code.springdemo.service.CategoryService;
import com.luv2code.springdemo.service.OrderService;
import com.luv2code.springdemo.service.ProductService;
import com.luv2code.springdemo.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	
	@PostMapping("/add/{id}")
	public @ResponseBody String showLoginForm(@PathVariable("id") int id) {
		session.setAttribute("cartList", true);
		return "("+Integer.toString(cartService.addProduct(id))+")";
	}
	
	@GetMapping
	public String showCart(Model theModel) {
		
		List<Category> theCategories = categoryService.getCategories();
		
		theModel.addAttribute("categories",  theCategories);
		
		HashMap<Integer, Integer> productsMap = cartService.getProducts();	
		
		if(productsMap!=null) {
			
			Set<Integer> ids=productsMap.keySet();
			List<Product> productsByIds = productService.getProdactsByIds(ids);

			theModel.addAttribute("productsByIds", productsByIds);
			theModel.addAttribute("totalPrice", cartService.getTotalPrice(productsByIds));
		}
		
		return "cart/index";
	}
	
	@GetMapping("/delete/{id}")
	public String removeProductFromCart(@PathVariable("id") int id) {
		
		cartService.removeProductFromCart(id);
		
		return "redirect:/cart";
		
	}
	
	//@RequestMapping("/checkout")
	//public String checkoutOrder(HttpSession session,Model theModel,@Valid @ModelAttribute("order") Order order,
	//BindingResult theBindingResult) 
	
	@GetMapping("/checkout")
	public String checkoutOrder(Model theModel)
	{
		
		HashMap<Integer, Integer> productsMap = cartService.getProducts();
		
		Set<Integer> ids=productsMap.keySet();
		List<Product> productsByIds = productService.getProdactsByIds(ids);

		theModel.addAttribute("totalPrice", cartService.getTotalPrice(productsByIds));
		
		List<Category> theCategories = categoryService.getCategories();
		theModel.addAttribute("categories",  theCategories);
			
		theModel.addAttribute("errorExist", true);
		if(session.getAttribute("usersId")==null) {
				theModel.addAttribute("order", new Order());
		}
		else {
			User user = userService.getUserById((Integer)session.getAttribute("usersId"));
			theModel.addAttribute("order", new Order(user.getName()));
		}
		return "cart/checkout";
	}
	
	@PostMapping("/checkout/process")
	public String checkoutOrderProcess(Model theModel,@Valid @ModelAttribute("order") Order order,
	BindingResult theBindingResult) {
		
		HashMap<Integer, Integer> productsMap = cartService.getProducts();
		
		Set<Integer> ids=productsMap.keySet();
		List<Product> productsByIds = productService.getProdactsByIds(ids);
		
		theModel.addAttribute("totalPrice", cartService.getTotalPrice(productsByIds));
		
		List<Category> theCategories = categoryService.getCategories();
		theModel.addAttribute("categories",  theCategories);
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("errorExist", true);
			System.out.println(theBindingResult);
			return "cart/checkout";
		}
		else {
			theModel.addAttribute("errorExist", false);
			Instant now = Instant.now();
			ZoneId zoneId = ZoneId.of("Europe/Minsk"); 
			ZonedDateTime dateAndTime = ZonedDateTime.ofInstant(now, zoneId);
			order.setDateTime(dateAndTime);
			order.setUserId((Integer)session.getAttribute("usersId"));
			orderService.saveOrder(order);
			session.setAttribute("products", null);
			session.setAttribute("countItrems", null);
		}
			
		
		
		return "cart/checkout";
	}
}
