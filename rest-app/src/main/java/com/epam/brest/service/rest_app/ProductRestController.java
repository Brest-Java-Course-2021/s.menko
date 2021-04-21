package com.epam.brest.service.rest_app;

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

import com.epam.brest.model.Product;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ProductService;
import com.epam.brest.service.impl.CategoryServiceImpl;
import com.epam.brest.service.impl.ProductServiceImpl;
import com.epam.brest.service.rest_app.exception.NotFoundException;


@RestController
@RequestMapping("/api")
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	//
	@GetMapping("/products")
	public List<Product> getProducts() {
		return productService.getProdactList();
	}
	//
	@GetMapping("/products/totalProducts/{categoryId}")
	public int getTotalProdactsInCategory(@PathVariable int categoryId) {
		if(!categoryService.checkCategoryExist(categoryId))
			throw new NotFoundException("Category id not found - " + categoryId);
		return productService.getTotalProdactsInCategory(categoryId);
		
	}
	//
	@GetMapping("/products/latest")
	public List<Product> getLatestProducts() {
		return productService.getLatestProducts();
	}
	//
	@GetMapping("/products/{productId}")
	public Product getProduct(@PathVariable int productId) {
		Product theProducts = productService.getProdactById(productId).orElseThrow(() -> new NotFoundException("Product id not found - " + productId));
		return theProducts;
	}
	//
	@GetMapping("/products/category-{categoryId}/page-{number}")
	public List<Product> getProduct(@PathVariable int categoryId,@PathVariable int number) {
		if(!categoryService.checkCategoryExist(categoryId))
			throw new NotFoundException("Category id not found - " + categoryId);
		List<Product> theProducts = productService.getProdactsListByCategory(categoryId, number);
		return theProducts;
	}
	//
	@PostMapping("/products/byIds")
	public List<Product> getProductbyIds(@RequestBody Set<Integer> ids) {
		List<Product> theProducts = productService.getProdactsByIds(ids);
		return theProducts;
	}
	
	
	//
	@PostMapping("/products")
	public Product addProduct(@RequestBody Product theProduct) {
	
		theProduct.setId(0);
		productService.saveProduct(theProduct);
		return theProduct;
	}
	//
	@PutMapping("/products")
	public Product updateProduct(@RequestBody Product theProduct) {
		if(!productService.checkProductExist(theProduct.getId()))
			throw new NotFoundException("Product id not found - " + theProduct.getId());
		productService.saveProduct(theProduct);
		return theProduct;
		
	}
	//
	@DeleteMapping("/products/{productId}")
	public String deleteProduct(@PathVariable int productId) {
		if(!productService.checkProductExist(productId))
			throw new NotFoundException("Product id not found - " + productId);
		productService.deleteProduct(productId);
		return "Deleted product id - " + productId;
	}
	//
	@GetMapping("/products/ProdactsFromOrder/{orderId}")
	public List<Product> getProdactsFromOrder(@PathVariable int orderId) {
		return productService.getProdactsFromOrder(orderId);
	}
	
	@GetMapping("/products/productInOrder/{productId}")
	public boolean productInOrderExist(@PathVariable int productId) {
		if(!productService.checkProductExist(productId))
			throw new NotFoundException("Product id not found - " + productId);
		return productService.productInOrderExist(productId);
	}
	//
	@GetMapping("/productCheck/{productId}")
	public boolean checkProductExist(@PathVariable int productId) {
		
		return productService.checkProductExist(productId);
			
	}
	
}


















