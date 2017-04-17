package com.lucjross.java8lombokstreamsexample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeManagerTest {

    private static final String ssn = "111-11-1111";
    private EmployeeManager employeeManager;
    private Company company = new Company("Acme");
    private Employee employee = new Employee(ssn);

    @Before
    public void before() {
        employeeManager = new EmployeeManager();
        company.setDescription("Manufacturer of goods");
    }

    @Test
    public void testAddCompany() {
        assertTrue(employeeManager.addCompany(company));
        assertTrue(employeeManager.getEmployees(company).size() == 0);
        assertFalse(employeeManager.addCompany(company));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCompany_NotExists() {
        employeeManager.updateCompany(company);
    }

    @Test
    public void testUpdateCompany_Exists() {
        employeeManager.addCompany(company);
        Company update = new Company(company.getName());
        String newDesc = "Retailer of goods";
        update.setDescription(newDesc);
        employeeManager.updateCompany(update);
        Company c = employeeManager.getOrFail(company);
        assertEquals(newDesc, c.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCompany_NotExists() {
        employeeManager.deleteCompany(company);
    }

    @Test
    public void testDeleteCompany_Exists() {
        employeeManager.addCompany(company);
        employeeManager.deleteCompany(company);
        assertTrue(employeeManager.addCompany(company));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmployee_CompanyNotExists() {
        employeeManager.addEmployee(company, employee);
    }

    @Test
    public void testAddEmployee_CompanyExists() {
        employeeManager.addCompany(company);
        employeeManager.addEmployee(company, employee);
        assertEquals(1, employeeManager.getEmployees(company).size());
        assertTrue(employeeManager.getEmployees(company).contains(employee));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmployee_EmployeeNotExists() {
        employeeManager.addCompany(company);
        employeeManager.updateEmployee(company, employee);
    }

    @Test
    public void testUpdateEmployee_EmployeeExists() {
        employeeManager.addCompany(company);
        employee.setSalary(1);
        employeeManager.addEmployee(company, employee);

        Employee update = new Employee(ssn);
        update.setSalary(2);
        employeeManager.updateEmployee(company, update);
        assertEquals(0, employeeManager.getEmployees(company).stream().filter(e -> e.getSalary() == 1).count());
        assertEquals(1, employeeManager.getEmployees(company).stream().filter(e -> e.getSalary() == 2).count());
    }
}
