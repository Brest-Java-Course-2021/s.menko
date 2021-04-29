package com.luv2code.springdemo.rest;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springdemo.entity.Product;
import com.luv2code.springdemo.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductRestController {

	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public List<Product> getProducts() {
		return productService.getProdactList();
	}
	
	@GetMapping("/products/totalProducts/{categoryId}")
	public int getProducts(@PathVariable int categoryId) {
		
		return productService.getTotalProdactsInCategory(categoryId);
		
	}
	
	@GetMapping("/products/latest")
	public List<Product> getLatestProducts() {
		return productService.getLatestProducts();
	}
	
	@GetMapping("/products/{productId}")
	public Product getProduct(@PathVariable int productId) {
		Product theProducts = productService.getProdactById(productId);
		return theProducts;
	}
	
	@GetMapping("/products/category-{categoryId}/page-{number}")
	public List<Product> getProduct(@PathVariable int categoryId,@PathVariable int number) {
		List<Product> theProducts = productService.getProdactsListByCategory(categoryId, number);
		return theProducts;
	}
	
	@PostMapping("/products/byIds")
	public List<Product> getProductbyIds(@RequestBody Set<Integer> ids) {
		List<Product> theProducts = productService.getProdactsByIds(ids);
		return theProducts;
	}
	
	
	
	@PostMapping("/products")
	public Integer addProduct(@RequestBody Product theProduct) {
	
		theProduct.setId(0);
		productService.saveProduct(theProduct);
		return theProduct.getId();
	}
	
	@PutMapping("/products")
	public Integer updateProduct(@RequestBody Product theProduct) {
		
		productService.saveProduct(theProduct);
		return theProduct.getId();
		
	}
	
	@DeleteMapping("/products/{productId}")
	public String deleteCategory(@PathVariable int productId) {

		productService.deleteProduct(productId);
		return "Deleted category id - " + productId;
	}
	
	@GetMapping("/products/ProdactsFromOrder/{orderId}")
	public List<Product> getProdactsFromOrder(@PathVariable int orderId) {
		return productService.getProdactsFromOrder(orderId);
	}
	
	@GetMapping("/products/productInOrderExist/{productId}")
	public boolean productInOrderExist(@PathVariable int productId) {
		return productService.productInOrderExist(productId);
	}
	
}


















