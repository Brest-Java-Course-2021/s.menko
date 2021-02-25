package com.epam.brest.model;

import org.junit.Assert;
import org.junit.Test;

public class DepartmentTest {

    @Test
    public void getDepartmentNameConstructor() {
        Department department = new Department("IT");
        Assert.assertEquals("IT", department.getDepartmentName());
    }

    @Test
    public void getDepartmentNameSetter() {
        Department department = new Department();
        department.setDepartmentName("IT");
        Assert.assertEquals("IT", department.getDepartmentName());
    }
}