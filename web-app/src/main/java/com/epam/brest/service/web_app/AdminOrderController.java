package com.epam.brest.service.web_app;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.epam.brest.model.DataOrderInterval;
import com.epam.brest.model.Order;
import com.epam.brest.model.Product;
import com.epam.brest.service.OrderService;
import com.epam.brest.service.ProductService;



@Controller
@RequestMapping("/admin/order") 
public class AdminOrderController extends AdminBase{
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping
	public String showOrderList(Model theModel)  {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			theModel.addAttribute("dataFormat",formatter);
			List<Order> theOrders = orderService.getOrders();
			theModel.addAttribute("orders",theOrders);
			theModel.addAttribute("dataInterval",new DataOrderInterval());
			theModel.addAttribute("dataValidation",true);
		
		return "admin_order/index";
	}
	
	
	@PostMapping
	public String showOrderInterval(Model theModel,@ModelAttribute("dataInterval") DataOrderInterval dataInterval) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		ZonedDateTime startData=dataInterval.getStartData();
		ZonedDateTime endData=dataInterval.getEndData();
		boolean validation = true;
		
		if(endData!=null&&startData!=null&&startData.isAfter(endData)) 
			validation=false;
		System.out.println(startData);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		theModel.addAttribute("dataFormat",formatter);
		List<Order> theOrders=Collections.emptyList();
		if(validation)
			theOrders = orderService.getOrdersFromInterval(dataInterval);
		
		theModel.addAttribute("dataValidation",validation);
		theModel.addAttribute("orders",theOrders);
	
		
		return "admin_order/index";
	}
	
	@GetMapping("/delete")
	public String deleteOrder(@RequestParam("orderId") int theId) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		orderService.deleteOrder(theId);
		
		return "redirect:/admin/order";
	}
	
	@GetMapping("/update")
	public String showFormForUpdate(@RequestParam("orderId") int theId,
									Model theModel) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		Order theOrder = orderService.getOrderById(theId).get();	
		theModel.addAttribute("order", theOrder);	
		return "admin_order/update";
	}
	
	@PostMapping("/save")
	public String saveOrder(Model theModel, @Valid @ModelAttribute("order") Order theOrder, 
			BindingResult theBindingResult) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("order", theOrder);
		}
		else {
			orderService.updateOrder(theOrder);  
			return "redirect:/admin/order";
		}

		return "admin_order/update";
	}
	
	@GetMapping("/view")
	public String viewOrder(@RequestParam("orderId") int theId, Model theModel) {
		
		if(!checkLoggedByAdmin()) {
			return "redirect:/admin";
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		theModel.addAttribute("dataFormat",formatter);
		Order theOrder = orderService.getOrderById(theId).get();	
		theModel.addAttribute("order", theOrder);
		theModel.addAttribute("orderService",orderService);
		theModel.addAttribute("totalPrice",orderService.totalPrice(theId));
		List<Product> products =  productService.getProdactsFromOrder(theOrder.getId());
		theModel.addAttribute("products", products);
		Map<Integer,Integer> quantityMap = orderService.getQuantity(theId);
		theModel.addAttribute("quantity", quantityMap);
		return "admin_order/view";
	}
	
	

}
