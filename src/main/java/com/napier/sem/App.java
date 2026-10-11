package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Connects to and reads employee records from the MySQL database.
 */
public class App {
    private Connection con = null;

    /**
     * Connect to the MySQL database, retrying while the database starts.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
            return;
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(30000);
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example");
                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Database connection retry interrupted");
                return;
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                System.out.println("Error closing connection to database");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Find an employee by ID, including their current title, salary,
     * department and department manager.
     *
     * @param id employee number to look up
     * @return the employee record, or null if no matching employee was found
     */
    public Employee getEmployee(int id) {
        String query =
                "SELECT e.emp_no, e.first_name, e.last_name, "
                        + "t.title, s.salary, d.dept_name, "
                        + "CONCAT_WS(' ', manager.first_name, manager.last_name) AS manager "
                        + "FROM employees e "
                        + "LEFT JOIN titles t ON e.emp_no = t.emp_no "
                        + "AND t.to_date = '9999-01-01' "
                        + "LEFT JOIN salaries s ON e.emp_no = s.emp_no "
                        + "AND s.to_date = '9999-01-01' "
                        + "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no "
                        + "AND de.to_date = '9999-01-01' "
                        + "LEFT JOIN departments d ON de.dept_no = d.dept_no "
                        + "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no "
                        + "AND dm.to_date = '9999-01-01' "
                        + "LEFT JOIN employees manager ON dm.emp_no = manager.emp_no "
                        + "WHERE e.emp_no = ?";

        if (con == null) {
            System.out.println("Cannot get employee details: not connected to database");
            return null;
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                Employee employee = new Employee();
                employee.emp_no = result.getInt("emp_no");
                employee.first_name = result.getString("first_name");
                employee.last_name = result.getString("last_name");
                employee.title = result.getString("title");
                employee.salary = result.getInt("salary");
                employee.dept_name = result.getString("dept_name");
                employee.manager = result.getString("manager");
                return employee;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Print an employee record to the console.
     *
     * @param employee record to display
     */
    public void displayEmployee(Employee employee) {
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println(
                employee.emp_no + " "
                        + employee.first_name + " "
                        + employee.last_name + "\n"
                        + employee.title + "\n"
                        + "Salary: " + employee.salary + "\n"
                        + employee.dept_name + "\n"
                        + "Manager: " + employee.manager + "\n");
    }

    public static void main(String[] args) {
        App app = new App();
        app.connect();
        if (app.con == null) {
            System.err.println("Could not establish database connection");
            System.exit(1);
        }

        int employeeId = args.length > 0 ? Integer.parseInt(args[0]) : 255530;
        Employee employee = app.getEmployee(employeeId);
        app.displayEmployee(employee);
        app.disconnect();
    }
}
