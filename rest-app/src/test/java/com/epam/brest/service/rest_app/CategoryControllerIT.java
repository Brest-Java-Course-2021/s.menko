package com.epam.brest.service.rest_app;

import com.epam.brest.dao.hiberbate.CategoryDAOImpl;
import com.epam.brest.model.Category;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CategoryControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryControllerIT.class);

    @Autowired
    private CategoryRestController categoryController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockCategoryService categoryService = new  MockCategoryService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(categoryController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(restExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void getCategoriesTest() throws Exception {
            List<Category> categories = categoryService.getCategories();
            assertNotNull(categories);
            assertTrue(categories.size() > 0);
    }
    
    @Test
    public void getCategoryByIdTest() throws Exception {
    	 List<Category> categories = categoryService.getCategories();
         Assertions.assertNotNull(categories);
         assertTrue(categories.size() > 0);

         Integer categoryId = categories.get(0).getId();
         Category expCategory = categoryService.getCategoryById(categoryId).get();
         Assertions.assertEquals(categoryId, expCategory.getId());
         Assertions.assertEquals(categories.get(0).getName(), expCategory.getName());
         Assertions.assertEquals(categories.get(0), expCategory);
    }
    

    @Test
    public void shouldReturnNotFoundOnMissedCategory() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/categories/999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void saveCategoryTest() throws Exception {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        categoryService.saveCategory(new Category("Блузки",2,1));

        List<Category> realCategories = categoryService.getCategories();
        Assertions.assertEquals(categories.size() + 1, realCategories.size());
    }
    
    @Test
    public void updateCategoryTest() throws Exception {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        Category category = categories.get(0);
        category.setName("TEST_CATEGORY");
        categoryService.updateCategory(category);

        Optional<Category> realCategory = categoryService.getCategoryById(category.getId());
        Assertions.assertEquals("TEST_CATEGORY", realCategory.get().getName());
    
   
    }
    
    @Test
    public void shouldReturnNotFoundOnUpdateCategory() throws Exception {

    	Category updateCategory = new Category("Блузки",2,1);
    	updateCategory.setId(999);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put(
        		"/api/categories")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategory))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void deleteCategoryTest() throws Exception {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        categoryService.deleteCategory(2);

        List<Category> realCategories = categoryService.getCategories();
        Assertions.assertEquals(categories.size()-1, realCategories.size());
    }
    
    @Test
    public void shouldReturnNotFoundOnDeleteCategory() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(
        		"/api/categories/999")
        		.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    @Test
    public void checkCategoryExistTest() throws Exception {
        assertTrue(categoryService.checkCategoryExist(1));
        assertTrue(!categoryService.checkCategoryExist(999));
    }
    
    @Test
    public void productOfCategoryInOrderExistTest() throws Exception {
        assertTrue(categoryService.productOfCategoryInOrderExist(1));
        assertTrue(!categoryService.productOfCategoryInOrderExist(2));
    }
    
    @Test
    public void shouldReturnNotFoundOnCategoryInOrderExist() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
        		"/api/CategoryInOrder/999")
        		.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }
    
    /////////////////////////////////////////////////////////////////////////////////

    private class MockCategoryService {

        public List<Category> getCategories() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get("/api/categories")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
           

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Category>>() {});
        }

        public Optional<Category> getCategoryById(Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get("/api/categories/"  + categoryId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Category.class));
        }
        
        public Category saveCategory(Category category) throws Exception {
            String json = objectMapper.writeValueAsString(category);
            MockHttpServletResponse response =
                    mockMvc.perform(post("/api/categories/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Category.class);
        }
        
        public Category updateCategory(Category category) throws Exception {
        	System.out.println(objectMapper.writeValueAsString(category)+"sss");
            MockHttpServletResponse response =
                    mockMvc.perform(put("/api/categories/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(category))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            System.out.println(response.getContentAsString());
            return objectMapper.readValue(response.getContentAsString(), Category.class);
        }
        
        public String deleteCategory (Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete("/api/categories/"  + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), String.class);
        }
        
        public Boolean checkCategoryExist(Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
            		MockMvcRequestBuilders.get("/api/categoryCheck/"+categoryId)
            		.contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Boolean.class);
        }
        
        public Boolean productOfCategoryInOrderExist(Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
            		MockMvcRequestBuilders.get("/api/CategoryInOrder/"+categoryId)
            		.contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Boolean.class);
        }
        
    }
}
