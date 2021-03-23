package com.luv2code.springdemo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;

public interface ProductService {
	
	public List<Product> getLatestProducts();
	public List<Product> getProdactsListByCategory(int category,int page);
	public Optional<Product> getProdactById(int id);
	public int getTotalProdactsInCategory(int category);
	public List<Product> getProdactsByIds(Set<Integer> ids);
	public List<Product> getProdactList();
	public void deleteProduct(int id);
	public int saveProduct(Product theProduct);
	public void saveImage(MultipartFile img,int id)throws IOException;
	public String getImage(int id);
	public List<Product> getProdactsFromOrder(int orderId);
	public boolean productInOrderExist(int idProduct);
	public boolean checkProductExist(int id);
}
