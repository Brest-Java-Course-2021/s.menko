package com.epam.brest.service.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.epam.brest.model.Product;
import com.epam.brest.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	private RestTemplate restTemplate;
	private String crmRestUrl="http://localhost:8080/api/products";
	
	@Autowired
	public ProductServiceImpl(RestTemplate theRestTemplate) {
		 restTemplate=theRestTemplate;
	}
	
	@Override
	public List<Product> getProdactsFromOrder(int orderId) {
		
		ResponseEntity<List<Product>> responseEntity = 
				restTemplate.exchange(crmRestUrl+"/ProdactsFromOrder/"+orderId, HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Product>>() {});
		List<Product> products = responseEntity.getBody();

		return products;
	}
	
	@Override
	public int getTotalProdactsInCategory(int category) {
		Integer totalProduct = 
				restTemplate.getForObject(crmRestUrl + "/totalProducts/" + category, 
						Integer.class);
		return totalProduct;
	}
	
	@Override
	public List<Product> getLatestProducts() {
		ResponseEntity<List<Product>> responseEntity = 
				restTemplate.exchange(crmRestUrl+"/latest", HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Product>>() {});
		List<Product> products = responseEntity.getBody();

		return products;
	}
	
	@Override
	public List<Product> getProdactsListByCategory(int category,int page){
		
		ResponseEntity<List<Product>> responseEntity = 
				restTemplate.exchange(crmRestUrl+"/category-"+category+"/page-"+page, HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Product>>() {});
		List<Product> products = responseEntity.getBody();

		return products;
	}
	
	@Override
	public Optional<Product> getProdactById(int id) {
		Product theProduct= 
				restTemplate.getForObject(crmRestUrl + "/" + id, 
						Product.class);
		
		return Optional.ofNullable(theProduct);
	}

	@Override
	public List<Product> getProdactsByIds(Set<Integer> ids) {
		RequestEntity<Set<Integer>> request = RequestEntity
	            .post(URI.create(crmRestUrl+"/byIds"))
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(ids);
	ResponseEntity<List<Product>> response = restTemplate.exchange(
				crmRestUrl+"/byIds", 
	            HttpMethod.POST, 
	            request, 
	            new ParameterizedTypeReference<List<Product>>() {}
	            );
	return response.getBody();
	}

	@Override
	public List<Product> getProdactList() {
		
		ResponseEntity<List<Product>> responseEntity = 
				restTemplate.exchange(crmRestUrl, HttpMethod.GET, null, 
									  new ParameterizedTypeReference<List<Product>>() {});
		List<Product> products = responseEntity.getBody();

		return products;
	}

	@Override
	public void deleteProduct(int id) {
		
		restTemplate.delete(crmRestUrl + "/" + id);
	}

	@Override
	public int saveProduct(Product theProduct) {
		
		int productId = theProduct.getId();
		if (productId == 0) {
			int newId = restTemplate.postForEntity(crmRestUrl, theProduct, Product.class).getBody().getId();
			return newId;			
		} else {
			restTemplate.put(crmRestUrl, theProduct);
			return  productId;
		}
		
	}

	/*@Override
	public void saveImage(MultipartFile img, int id) throws IOException {
		
		if(img!=null&&!img.isEmpty()) {
	    File upl = new File("src/main/webapp/resources/images/products/"+id+".jpg");
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
		
		
		File file = new File("src/main/webapp/"+pathtoImage);
		
		if(file.exists())
			return "/"+pathtoImage;

		
		return "/"+path+noImage;
	}*/
	
	
	@Override
	public void saveImage(MultipartFile img, int id) throws IOException {
		
		if(img!=null&&!img.isEmpty()) {
	    File upl = new File("web-app-1.0-SNAPSHOT/resources/images/products/"+id+".jpg");
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
		
		
		File file = new File("web-app-1.0-SNAPSHOT/"+pathtoImage);
		
		if(file.exists())
			return "/"+pathtoImage;

		
		return "/"+path+noImage;
	}

	@Override
	public boolean productInOrderExist(int idProduct) {
		
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/productInOrder/" + idProduct, 
						Boolean.class);
		
		return result;
	}

	@Override
	public boolean checkProductExist(int id) {
		Boolean result = 
				restTemplate.getForObject(crmRestUrl+"/productCheck/" + id,Boolean.class);
		return result;
	}
}
