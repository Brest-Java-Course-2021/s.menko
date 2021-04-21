package com.epam.brest.service.rest_app;

import com.epam.brest.dao.hiberbate.CategoryDAOImpl;
import com.epam.brest.model.Category;
import com.epam.brest.model.Product;
import com.epam.brest.service.impl.CategoryServiceImpl;
import com.epam.brest.service.rest_app.exception.RestExceptionHandler;
import com.epam.brest.testdb.SpringJdbcConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ProductControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductControllerIT.class);

    @Autowired
    private ProductRestController productController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockProductService productService = new  MockProductService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(productController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(restExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void getTotalProdactsInCategoryTest() throws Exception {
        int count = productService.getTotalProdactsInCategory(1);
        assertTrue(count > 0);
    }
    
    @Test
    public void shouldReturnNotFoundOnTotalProductInCategory() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/products/totalProducts/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void getProdactListTest() throws Exception {
        List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void getLatestProductsTest() throws Exception {
    	List<Product> products = productService.getLatestProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void getProductByIdTest() throws Exception {
    	 List<Product> products = productService.getProdactList();
         Assertions.assertNotNull( products);
         assertTrue( products.size() > 0);

         Integer productId =  products.get(0).getId();
         Product expProduct = productService.getProductById(productId).get();
         Assertions.assertEquals(productId,  expProduct.getId());
         Assertions.assertEquals(products.get(0).getName(),  expProduct.getName());
         Assertions.assertEquals(products.get(0),  expProduct);
    }
    
    @Test
    public void shouldReturnNotFoundOnProductById() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/products/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void getProdactsListByCategoryTest() throws Exception {
        List<Product> products = productService.getProdactsListByCategory(1,2);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void shouldReturnNotFoundCategoryOnProductsListByCategory() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/products/category-999/page-1")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void getProdactsByIdsTest() throws Exception {
    	
    	HashSet<Integer> ids = new HashSet<Integer>();
    	ids.add(1);
    	ids.add(2);
    	ids.add(3);
        List<Product> products = productService.getProdactsByIds(ids);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void saveProductTest() throws Exception {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);


        productService.saveProduct(new Product("Мужская футболка", 123.99, 1, 
        		"Itachi","Футболка 3D — это удобная футболка прямого кроя", 3,"no-image"));

        List<Product> realProducts = productService.getProdactList();
        Assertions.assertEquals(products.size() + 1, realProducts.size());
    }
    
    @Test
    public void updateProductTest() throws Exception {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);

        Product product = products.get(0);
        product.setName("TEST_PRODUCT");
        product = productService.updateProduct(product);

        Optional<Product> realProduct = productService.getProductById(product.getId());
        Assertions.assertEquals("TEST_PRODUCT", realProduct.get().getName());
    }
    
    @Test
    public void shouldReturnNotFoundProductOnUpdateProduct() throws Exception {

    	Product product = new Product("Мужская футболка", 123.99, 1, 
        		"Itachi","Футболка 3D — это удобная футболка прямого кроя", 2,"no-image");
    	product.setId(999);
    			MockHttpServletResponse response =
                mockMvc.perform(put("/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                        .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void deleteProductTest() throws Exception {
    	List<Product> products = productService.getProdactList();
        assertNotNull(products);
        assertTrue(products.size() > 0);

        productService.deleteProduct(8);
        List<Product> realProducts = productService.getProdactList();
        Assertions.assertEquals(products.size()-1, realProducts.size());
    }
    
    @Test
    public void shouldReturnNotFoundProductOndDelete() throws Exception {

    	MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/products/999")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void getProdactsFromOrderTest() throws Exception {
        List<Product> products = productService.getProdactsFromOrder(1);
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }
    
    @Test
    public void checkProductExistTest() throws Exception {
        assertTrue(productService.checkProductExist(2));
        assertTrue(!productService.checkProductExist(999));
    }
    
    @Test
    public void productInOrderExistTest() throws Exception {
        assertTrue(productService.productInOrderExist(2));
        assertTrue(!productService.productInOrderExist(6));
    }
    
    @Test
    public void shouldReturnNotFoundProductInOrderExist() throws Exception {

    	MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/productInOrder/999")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    /////////////////////////////////////////////////////////////////////////////////

    private class MockProductService {
    	
    	 public Integer getTotalProdactsInCategory(Integer categoryId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products/totalProducts/"  + categoryId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Integer.class);
         }
    	 
    	 public List<Product> getProdactList() throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products")
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             assertNotNull(response);
            

             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
         }
    	 
    	 public List<Product> getLatestProducts() throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products/latest")
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             assertNotNull(response);
            

             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
         }
    	 
    	 public Optional<Product> getProductById(Integer productId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products/"  + productId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return Optional.of(objectMapper.readValue(response.getContentAsString(), Product.class));
         }
    	 
    	 public List<Product> getProdactsListByCategory(int categoryId, int number) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products/category-"+categoryId+"/page-"+number)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             assertNotNull(response);
            

             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
         }
    	 
    	 public List<Product> getProdactsByIds(Set<Integer> ids) throws Exception {
             String json = objectMapper.writeValueAsString(ids);
             MockHttpServletResponse response =
                     mockMvc.perform(post("/api/products/byIds")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json)
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
         }
    	 
    	 public Product saveProduct(Product product) throws Exception {
             String json = objectMapper.writeValueAsString(product);
             MockHttpServletResponse response =
                     mockMvc.perform(post("/api/products/")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json)
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Product.class);
         }
    	 
    	 public Product updateProduct(Product product) throws Exception {
             MockHttpServletResponse response =
                     mockMvc.perform(put("/api/products/")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(objectMapper.writeValueAsString(product))
                             .accept(MediaType.APPLICATION_JSON)
                     ).andExpect(status().isOk())
                             .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Product.class);
         }
    	 
    	 public String deleteProduct (Integer productId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(
                     MockMvcRequestBuilders.delete("/api/products/"  + productId)
                             .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();

             return objectMapper.readValue(response.getContentAsString(), String.class);
         }
    	 
    	 public List<Product> getProdactsFromOrder(int orderId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(get("/api/products/ProdactsFromOrder/"+orderId)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             assertNotNull(response);
             return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
         }
    	 public Boolean checkProductExist(Integer productId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(
             		MockMvcRequestBuilders.get("/api/productCheck/"+productId)
             		.contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Boolean.class);
         }
    	 
    	 public Boolean productInOrderExist(Integer productId) throws Exception {
             MockHttpServletResponse response = mockMvc.perform(
             		MockMvcRequestBuilders.get("/api/products/productInOrder/"+productId)
             		.contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
                     .andReturn().getResponse();
             return objectMapper.readValue(response.getContentAsString(), Boolean.class);
         }
    	 
    }
}
