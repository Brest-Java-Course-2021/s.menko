package com.epam.brest.service.impl;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({CategoryServiceImpl.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@Transactional
class CategoryServiceImplIT {

    @Autowired
    CategoryService categoryService;

    @Test
    public void getCategoriesTest() {
        List<Category> categories = categoryService.getCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }
    
    @Test
    public void getCategoryByIdTest() {
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
    public void getCategoryByIdExceptionalTest() {

        Optional<Category> optionalCategory = categoryService.getCategoryById(999);
        assertTrue(optionalCategory.isEmpty());
    }
    
    @Test
    public void saveCategoryTest() {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        categoryService.saveCategory(new Category("Блузки",2,1));;

        List<Category> realCategories = categoryService.getCategories();
        Assertions.assertEquals(categories.size() + 1, realCategories.size());
    }
    
    @Test
    public void updateCategoryTest() {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        Category category = categories.get(0);
        category.setName("TEST_CATEGORY");
        categoryService.saveCategory(category);

        Optional<Category> realCategory = categoryService.getCategoryById(category.getId());
        Assertions.assertEquals("TEST_CATEGORY", realCategory.get().getName());
    }
    
    @Test
    public void deleteCategoryTest() {
    	List<Category> categories = categoryService.getCategories();
        Assertions.assertNotNull(categories);
        assertTrue(categories.size() > 0);

        categoryService.deleteCategory(2);

        List<Category> realCategories = categoryService.getCategories();
        Assertions.assertEquals(categories.size()-1, realCategories.size());
    }
    
    @Test
    public void checkCategoryExistTest() {
        assertTrue(categoryService.checkCategoryExist(1));
        assertTrue(!categoryService.checkCategoryExist(999));
    }
    
    @Test
    public void productOfCategoryInOrderExistTest() {
        assertTrue(categoryService.productOfCategoryInOrderExist(1));
        assertTrue(!categoryService.productOfCategoryInOrderExist(2));
    }
   
}
