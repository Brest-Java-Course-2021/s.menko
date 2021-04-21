package com.epam.brest.service.impl;

import com.epam.brest.model.Product;
import com.epam.brest.service.ProductService;
import com.epam.brest.testdb.SpringJdbcConfig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({ProductServiceImpl.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@Transactional
class ProductServiceImplIT {
	
    @Autowired
    ProductService productService;

    @Test
    public void getTotalProdactsInCategoryTest() {
        int count = productService.getTotalProdactsInCategory(1);
        assertTrue(count > 0);
    }
    
    @Test
    public void getProdactsFromOrderTest() {
        List<Product> products = productService.getProdactsFromOrder(1);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    
    @Test
    public void getLatestProductsTest() {
    	List<Product> products = productService.getLatestProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    @Test
    public void getProdactsListByCategoryTest() {
        List<Product> products = productService.getProdactsListByCategory(1,2);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void getProductByIdTest() {
    	 List<Product> products = productService.getProdactList();
         Assertions.assertNotNull( products);
         assertTrue( products.size() > 0);

         Integer productId =  products.get(0).getId();
         Product expProduct = productService.getProdactById(productId).get();
         Assertions.assertEquals(productId,  expProduct.getId());
         Assertions.assertEquals(products.get(0).getName(),  expProduct.getName());
         Assertions.assertEquals(products.get(0),  expProduct);
    }
    
    @Test
    public void getProductByIdExceptionalTest() {

        Optional<Product> optionalProduct = productService.getProdactById(999);
        assertTrue(optionalProduct.isEmpty());
    }
    
    @Test
    public void getProdactsByIdsTest() {
    	
    	HashSet<Integer> ids = new HashSet<Integer>();
    	ids.add(1);
    	ids.add(2);
    	ids.add(3);
        List<Product> products = productService.getProdactsByIds(ids);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    @Test
    public void getProdactListTest() {
        List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void deleteProductTest() {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);

        productService.deleteProduct(8);
        List<Product> realProducts = productService.getProdactList();
        Assertions.assertEquals(products.size()-1, realProducts.size());
    }
    
    @Test
    public void saveProductTest() {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);


        productService.saveProduct(new Product("Мужская футболка", 123.99, 1, 
        		"Itachi","Футболка 3D — это удобная футболка прямого кроя", 2,"no-image"));

        List<Product> realProducts = productService.getProdactList();
        Assertions.assertEquals(products.size() + 1, realProducts.size());
    }
    
    @Test
    public void updateProductTest() {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);

        Product product = products.get(0);
        product.setName("TEST_PRODUCT");
        productService.saveProduct(product);

        Optional<Product> realProduct = productService.getProdactById(product.getId());
        Assertions.assertEquals("TEST_PRODUCT", realProduct.get().getName());
    }
    
    @Test
    public void productInOrderExistTest() {
        assertTrue(productService.productInOrderExist(2));
        assertTrue(!productService.productInOrderExist(999));
    }
    
    @Test
    public void checkProductExistTest() {
        assertTrue(productService.checkProductExist(2));
        assertTrue(!productService.checkProductExist(999));
    }
    
}
