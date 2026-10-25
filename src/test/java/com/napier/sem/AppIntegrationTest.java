package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppIntegrationTest {

    @Test
    void testGetEmployee() {
        App app = new App();

        // Connect to the database running in Docker
        app.connect("localhost:33060", 1000);

        try {
            Employee employee = app.getEmployee(10001);

            // Check that an employee was returned
            assertNotNull(employee, "Employee should exist in the database");

            // Check that the employee number is correct
            assertEquals(10001, employee.emp_no);

        } finally {
            // Always close the database connection
            app.disconnect();
        }
    }
}