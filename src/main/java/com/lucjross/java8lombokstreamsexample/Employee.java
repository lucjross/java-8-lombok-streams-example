package com.lucjross.java8lombokstreamsexample;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@RequiredArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"socialSecurityNumber"})
class Employee {

    /*
     * note to reviewer: I took the liberty of making SSN the identity of Employee,
     * since for the update/delete methods, we have to give the user some way of specifying which Employee
     */
    private final @NonNull String socialSecurityNumber;

    private String firstName;
    private String lastName;
    private Date birthDate;
    private Integer salary;
    private JobTitle jobTitle;
}
