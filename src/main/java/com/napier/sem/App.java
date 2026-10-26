        package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class App {
    private Connection con = null;

    public void connect(String location, int delay) {
        try {
            // Load database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            return;
        }

        int retries = 10;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                // Wait for the database to start
                Thread.sleep(delay);

                // Connect to the database
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example");

                System.out.println("Successfully connected");
                return;

            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + (i + 1));

                // Print the full SQL exception for debugging
                e.printStackTrace();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Database connection retry interrupted");
                return;
            }
        }

        System.out.println("Could not connect to database after retries");
    }

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
                    System.out.println("DEBUG: No employee found for ID " + id);
                    return null;
                }

                System.out.println("DEBUG: Employee found for ID " + id);

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

    public java.util.ArrayList<Employee> getSalariesByRole() {
        java.util.ArrayList<Employee> employees = new java.util.ArrayList<>();

        String query =
                "SELECT e.emp_no, e.first_name, e.last_name, "
                        + "t.title, s.salary, d.dept_name, "
                        + "CONCAT_WS(' ', manager.first_name, manager.last_name) AS manager "
                        + "FROM employees e "
                        + "JOIN titles t ON e.emp_no = t.emp_no "
                        + "AND t.to_date = '9999-01-01' "
                        + "JOIN salaries s ON e.emp_no = s.emp_no "
                        + "AND s.to_date = '9999-01-01' "
                        + "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no "
                        + "AND de.to_date = '9999-01-01' "
                        + "LEFT JOIN departments d ON de.dept_no = d.dept_no "
                        + "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no "
                        + "AND dm.to_date = '9999-01-01' "
                        + "LEFT JOIN employees manager ON dm.emp_no = manager.emp_no "
                        + "WHERE t.title = 'Manager'";

        if (con == null) {
            System.out.println("Cannot get Manager salaries: not connected to database");
            return employees;
        }

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Employee employee = new Employee();
                employee.emp_no = result.getInt("emp_no");
                employee.first_name = result.getString("first_name");
                employee.last_name = result.getString("last_name");
                employee.title = result.getString("title");
                employee.salary = result.getInt("salary");
                employee.dept_name = result.getString("dept_name");
                employee.manager = result.getString("manager");

                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve Manager salaries");
            e.printStackTrace();
        }

        return employees;
    }

    public void outputEmployees(
            java.util.ArrayList<Employee> employees, String filename) {

        File directory = new File("./reports");

        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Could not create reports directory");
            return;
        }

        File file = new File(directory, filename);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# Manager Salaries Report\n\n");
            writer.write("| Emp No | First Name | Last Name | Title | Salary | Department | Manager |\n");
            writer.write("|---:|---|---|---|---:|---|---|\n");

            for (Employee employee : employees) {
                writer.write("| " + employee.emp_no
                        + " | " + safeMarkdown(employee.first_name)
                        + " | " + safeMarkdown(employee.last_name)
                        + " | " + safeMarkdown(employee.title)
                        + " | " + employee.salary
                        + " | " + safeMarkdown(employee.dept_name)
                        + " | " + safeMarkdown(employee.manager)
                        + " |\n");
            }

            System.out.println("Report created: " + file.getAbsolutePath());
            System.out.println("Employees included: " + employees.size());

        } catch (IOException e) {
            System.out.println("Failed to write Manager Salaries report");
            e.printStackTrace();
        }
    }

    private String safeMarkdown(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("|", "\\|")
                .replace("\r", " ")
                .replace("\n", " ");
    }

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

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Successfully disconnected from database");
            } catch (SQLException e) {
                System.out.println("Error closing connection to database");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        App app = new App();

        if (args.length < 2) {
            app.connect("localhost:33061", 1000);
        } else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        if (app.con == null) {
            System.err.println("Could not establish database connection");
            System.exit(1);
        }

        try {
            int employeeId = args.length > 2
                    ? Integer.parseInt(args[2])
                    : 255530;

            Employee employee = app.getEmployee(employeeId);
            app.displayEmployee(employee);

            java.util.ArrayList<Employee> managers = app.getSalariesByRole();
            app.outputEmployees(managers, "ManagerSalaries.md");

        } finally {
            app.disconnect();
        }
    }
}