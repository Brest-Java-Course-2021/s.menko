package com.luv2code.springdemo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springdemo.dao.ProductDAO;
import com.luv2code.springdemo.entity.Order;
import com.luv2code.springdemo.entity.Product;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;
	
	@Override
	@Transactional
	public List<Product> getProdactsFromOrder(int orderId) {
		return productDAO.getProdactsFromOrder(orderId);
	}
	
	@Override
	@Transactional
	public int getTotalProdactsInCategory(int category) {
		return productDAO.getTotalProdactsInCategory(category);
	}
	
	@Override
	@Transactional
	public List<Product> getLatestProducts() {
		return productDAO.getLatestProducts();
	}
	
	@Override
	@Transactional
	public List<Product> getProdactsListByCategory(int category,int page){
		return productDAO.getProdactsListByCategory(category,page);
	}
	
	@Override
	@Transactional
	public Product getProdactById(int id) {
		return productDAO.getProdactById(id);
	}

	@Override
	@Transactional
	public List<Product> getProdactsByIds(Set<Integer> ids) {
		return productDAO.getProdactsByIds(ids);
	}

	@Override
	@Transactional
	public List<Product> getProdactList() {
		return productDAO.getProdactList();
	}

	@Override
	@Transactional
	public void deleteProduct(int id) {
		
		productDAO.deleteProduct(id);
	}

	@Override
	@Transactional
	public int saveProduct(Product theProduct) {
		return productDAO.saveProduct(theProduct);
		
	}

	@Override
	public void saveImage(MultipartFile img, int id) throws IOException {
		
		if(img!=null&&!img.isEmpty()) {
	    File upl = new File("resources/images/products/"+id+".jpg");
	    System.out.println(upl.getAbsolutePath());
	    upl.createNewFile();
	    FileOutputStream fout = new FileOutputStream(upl);
	    fout.write(img.getBytes());
	    fout.close();
		}
	}

	@Override
	public String getImage(int id) {
		
		String noImage="no-image.jpg";
		String path="resources/images/products/";
		String pathtoImage = path+id+".jpg";
		
		File file = new File(pathtoImage);
		
		if(file.exists())
			return "/web-customer-tracker/"+pathtoImage;

		
		return "/web-customer-tracker/"+path+noImage;
	}

	@Override
	@Transactional
	public boolean productInOrderExist(int idProduct) {
		// TODO Auto-generated method stub
		return productDAO.productInOrderExist(idProduct);
	}
}
