package com.lucjross.java8lombokstreamsexample;

import lombok.RequiredArgsConstructor;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class EmployeeManager {

    private final Map<Company, Set<Employee>> companyEmployeeMap;

    EmployeeManager() {
        this.companyEmployeeMap = new HashMap<>();
    }

    /**
     *
     * @param company
     * @return true if company was added, false if already exists
     */
    boolean addCompany(Company company) {
        return companyEmployeeMap.putIfAbsent(company, new HashSet<>()) == null;
    }

    void updateCompany(Company company) {
        Set<Employee> value = companyEmployeeMap.remove(getOrFail(company));
        companyEmployeeMap.put(company, value);
    }

    void deleteCompany(Company company) {
        companyEmployeeMap.remove(getOrFail(company));
    }

    boolean addEmployee(Company company, Employee employee) {
        Set<Employee> employees = companyEmployeeMap.get(getOrFail(company));
        return employees.add(employee);
    }

    void updateEmployee(Company company, Employee employee) {
        Set<Employee> employees = companyEmployeeMap.get(getOrFail(company));
        if (! employees.remove(employee)) {
            throw new IllegalArgumentException();
        }

        employees.add(employee);
    }

    /**
     *
     * @return search builder for chaining search parameters
     */
    SearchBuilder employeeSearch() {
        return new SearchBuilder();
    }

    Company getOrFail(Company company) {
        return companyEmployeeMap.keySet().stream()
                .filter(company::equals)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    /*
     * for testing
     */
    Set<Employee> getEmployees(Company company) {
        return companyEmployeeMap.get(company);
    }



    class SearchBuilder {

        private Stream<Map.Entry<Company, Employee>> stream;

        private SearchBuilder() {
            stream = companyEmployeeMap.entrySet().stream()
                    .flatMap(mapEntry -> mapEntry.getValue().stream()
                            .map(employee -> new AbstractMap.SimpleImmutableEntry<>(mapEntry.getKey(), employee)));
        }

        SearchBuilder lastNameIgnoreCase(String s) {
            stream = stream.filter(entry ->
                    entry.getValue().getLastName() != null && entry.getValue().getLastName().equalsIgnoreCase(s));
            return this;
        }

        SearchBuilder socialSecurityNumber(String s) {
            stream = stream.filter(entry ->
                    entry.getValue().getSocialSecurityNumber().equals(s));
            return this;
        }

        SearchBuilder salaryGTE(int i) {
            stream = stream.filter(entry ->
                    entry.getValue().getSalary() != null && entry.getValue().getSalary() >= i);
            return this;
        }

        SearchBuilder salaryLTE(int i) {
            stream = stream.filter(entry ->
                    entry.getValue().getSalary() != null && entry.getValue().getSalary() <= i);
            return this;
        }

        SearchBuilder ageGTE(long millis, Date atDate) {
            stream = stream.filter(entry ->
                    entry.getValue().getBirthDate() != null &&
                            atDate.getTime() - entry.getValue().getBirthDate().getTime() >= millis);
            return this;
        }

        SearchBuilder ageLTE(long millis, Date atDate) {
            stream = stream.filter(entry ->
                    entry.getValue().getBirthDate() != null &&
                            atDate.getTime() - entry.getValue().getBirthDate().getTime() <= millis);
            return this;
        }

        SearchBuilder jobTitleIgnoreCase(String s) {
            stream = stream.filter(entry ->
                    entry.getValue().getJobTitle() != null && entry.getValue().getJobTitle().toString().equalsIgnoreCase(s));
            return this;
        }

        SearchBuilder companyNameIgnoreCase(String s) {
            stream = stream.filter(entry ->
                    entry.getKey().getName().equalsIgnoreCase(s));
            return this;
        }

        /**
         * @return all employees matching the criteria supplied through the builder methods
         */
        Set<Employee> collect() {
            return stream.map(Map.Entry::getValue)
                    .collect(Collectors.toSet());
        }
    }
}
