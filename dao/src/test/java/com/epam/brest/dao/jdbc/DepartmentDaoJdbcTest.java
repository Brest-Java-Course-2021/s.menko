package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.DepartmentDao;
import com.epam.brest.model.Department;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
public class DepartmentDaoJdbcTest {

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void findAllTest() {
        List<Department> departments = departmentDao.findAll();
        Assert.assertNotNull(departments);
        Assert.assertTrue(departments.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Department> departments = departmentDao.findAll();
        Assert.assertNotNull(departments);
        Assert.assertTrue(departments.size() > 0);

        Integer departmentId = departments.get(0).getDepartmentId();
        Department expDepartment = departmentDao.findById(departmentId).get();
        Assert.assertEquals(departmentId, expDepartment.getDepartmentId());
        Assert.assertEquals(departments.get(0).getDepartmentName(), expDepartment.getDepartmentName());
        Assert.assertEquals(departments.get(0), expDepartment);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void findByIdExeptionalTest() {
        departmentDao.findById(999).get();
    }

    @Test
    public void createDepartmentTest() {
        List<Department> departments = departmentDao.findAll();
        Assert.assertNotNull(departments);
        Assert.assertTrue(departments.size() > 0);

        Department department = new Department("HR");
        departmentDao.create(department);

        List<Department> realDepartments = departmentDao.findAll();
        Assert.assertEquals(departments.size() + 1, realDepartments.size());
    }

}