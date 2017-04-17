package com.lucjross.java8lombokstreamsexample;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class EmployeeManagerSearchTest {

    private static final Date AGE_AT_DATE = Date.from(Instant.parse("2017-01-12T00:00:00.00Z"));

    private EmployeeManager employeeManager;

    @Before
    public void before() {

        Map<Company, Set<Employee>> map = Stream.of(0, 1, 2)
                .map(i -> {
                    Company company = new Company("company" + i);
                    company.setDescription(company.getName() + " description");
                    return company;
                })
                .collect(Collectors.toMap(
                        c -> c,
                        company -> Stream.of(0, 1, 2)
                                .map(i -> {
                                    Employee e = new Employee(company.getName() + " employee" + i);
                                    e.setSalary(10 + i);
                                    e.setBirthDate(Date.from(Instant.parse("2017-01-1" + i + "T00:00:00.00Z")));
                                    e.setFirstName("firstName" + i);
                                    e.setLastName("lastName" + i);
                                    e.setJobTitle(JobTitle.values()[i]);
                                    return e;
                                }).collect(Collectors.toSet())));

        employeeManager = new EmployeeManager(map);
    }

    @Test
    public void testNoFilter() {
        Set<Employee> employees = employeeManager.employeeSearch().collect();
        assertEquals(9, employees.size());
    }

    @Test
    public void testLastName() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .lastNameIgnoreCase("LASTNAME1")
                .collect();
        assertTrue(employees.stream().allMatch(e -> e.getLastName().equals("lastName1")));
    }

    @Test
    public void testSSN() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .socialSecurityNumber("COMPANY0 EMPLOYEE0")
                .collect();
        assertEquals(0, employees.size());

        employees = employeeManager.employeeSearch()
                .socialSecurityNumber("company0 employee0")
                .collect();
        assertEquals(1, employees.size());
    }

    @Test
    public void testSalaryGTE() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .salaryGTE(12)
                .collect();
        assertEquals(3, employees.size());
        assertTrue(employees.stream().allMatch(e -> e.getSalary() >= 12));
    }

    @Test
    public void testSalaryLTE() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .salaryLTE(10)
                .collect();
        assertEquals(3, employees.size());
        assertTrue(employees.stream().allMatch(e -> e.getSalary() <= 10));
    }

    @Test
    public void testAgeGTE() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .ageGTE(TimeUnit.DAYS.toMillis(2), AGE_AT_DATE)
                .collect();
        assertEquals(3, employees.size());
        assertTrue(employees.stream().allMatch(e -> e.getBirthDate().compareTo(AGE_AT_DATE) <= 0));
    }

    @Test
    public void testAgeLTE() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .ageLTE(0, AGE_AT_DATE)
                .collect();
        assertEquals(3, employees.size());
        assertTrue(employees.stream().allMatch(e -> e.getBirthDate().equals(AGE_AT_DATE)));
    }

    @Test
    public void testJobTitle() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .jobTitleIgnoreCase(JobTitle.EXECUTIVE.toString().toLowerCase())
                .collect();
        assertEquals(3, employees.size());
        assertTrue(employees.stream().allMatch(e -> e.getJobTitle() == JobTitle.EXECUTIVE));
    }

    @Test
    public void testCompany() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .companyNameIgnoreCase("COMPANY0")
                .collect();
        assertTrue(employees.stream().allMatch(e -> e.getSocialSecurityNumber().startsWith("company0")));
        assertEquals(3, employees.size());
    }

    @Test
    public void testCompoundSearch() {
        Set<Employee> employees = employeeManager.employeeSearch()
                .companyNameIgnoreCase("company0")
                .salaryGTE(10)
                .salaryLTE(12)
                .jobTitleIgnoreCase(JobTitle.EXECUTIVE.toString().toLowerCase())
                .ageGTE(0, AGE_AT_DATE)
                .ageLTE(TimeUnit.DAYS.toMillis(30), AGE_AT_DATE)
                .collect();
        assertEquals(1, employees.size());
        Employee e = employees.iterator().next();
        assertTrue(e.getSalary() >= 10);
        assertTrue(e.getSalary() <= 12);
        assertEquals(JobTitle.EXECUTIVE, e.getJobTitle());
        assertTrue(e.getBirthDate().compareTo(Date.from(Instant.parse("2017-01-10T00:00:00.00Z"))) >= 0);
        assertTrue(e.getBirthDate().compareTo(Date.from(Instant.parse("2017-01-12T00:00:00.00Z"))) <= 0);
    }
}
