package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void testDisplayEmployeeNull() {

        App app = new App();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));

            app.displayEmployee(null);

        } finally {
            System.setOut(originalOutput);
        }

        assertTrue(output.toString().contains("Employee not found."));
    }
    @Test
    void testDisplayEmployeeDetails() {
        App app = new App();

        Employee employee = new Employee();
        employee.emp_no = 10001;
        employee.first_name = "John";
        employee.last_name = "Smith";
        employee.title = "Engineer";
        employee.salary = 50000;
        employee.dept_name = "IT";
        employee.manager = "Jane Doe";

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));

            app.displayEmployee(employee);

        } finally {
            System.setOut(originalOutput);
        }

        String result = output.toString();

        assertTrue(result.contains("10001 John Smith"));
        assertTrue(result.contains("Engineer"));
        assertTrue(result.contains("Salary: 50000"));
        assertTrue(result.contains("IT"));
        assertTrue(result.contains("Manager: Jane Doe"));
    }
    @Test
    void testDisplayEmployeeWithMissingFields() {
        App app = new App();

        Employee employee = new Employee();
        employee.emp_no = 10002;
        employee.first_name = "Alice";
        employee.last_name = "Brown";
        employee.title = null;
        employee.salary = 0;
        employee.dept_name = null;
        employee.manager = null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));
            app.displayEmployee(employee);
        } finally {
            System.setOut(originalOutput);
        }

        String result = output.toString();

        assertTrue(result.contains("10002 Alice Brown"));
        assertTrue(result.contains("Salary: 0"));
        assertTrue(result.contains("null"));
    }
}

