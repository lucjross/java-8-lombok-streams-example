package com.lucjross.java8lombokstreamsexample;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

public class DomainTest {

    @Test(expected = NullPointerException.class)
    public void testCompanyCtorNPE() {
        new Company(null);
    }

    @Test
    public void testCompanyEquals() {
        Company c1 = new Company("Acme");
        c1.setDescription("xxx");
        Company c2 = new Company("Acme");
        c2.setDescription("yyy");
        Assert.assertEquals(c1, c2);
    }

    @Test(expected = NullPointerException.class)
    public void testEmployeeCtorNPE() {
        new Employee(null);
    }

    @Test
    public void testEmployeeEquals() {
        Employee e1 = new Employee("ssn");
        e1.setJobTitle(JobTitle.EXECUTIVE);
        e1.setFirstName("xxx");
        e1.setLastName("xxx");
        e1.setBirthDate(Date.from(Instant.EPOCH));
        e1.setSalary(1);
        Employee e2 = new Employee("ssn");
        e2.setJobTitle(JobTitle.MANAGER);
        e2.setFirstName("YYY");
        e2.setLastName("YYY");
        e2.setBirthDate(Date.from(Instant.EPOCH.plusSeconds(1)));
        e2.setSalary(2);
        Assert.assertEquals(e1, e2);
    }
}
