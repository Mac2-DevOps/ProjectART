package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private Connection con = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            return;
        }

        int retries = 10;
        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example");
                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(e.getMessage());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    System.out.println("Database connection retry interrupted");
                    return;
                }
            }
        }

        System.out.println("Could not connect to database after retries");
    }


    public Employee getEmployee(int id) {
        String query =
                "SELECT e.emp_no, e.first_name, e.last_name, "
                        + "t.title, s.salary, "
                        + "d.dept_no, d.dept_name, "
                        + "manager.emp_no AS manager_emp_no, "
                        + "manager.first_name AS manager_first_name, "
                        + "manager.last_name AS manager_last_name "
                        + "FROM employees e "
                        + "LEFT JOIN titles t ON e.emp_no = t.emp_no "
                        + "AND t.to_date = '9999-01-01' "
                        + "LEFT JOIN salaries s ON e.emp_no = s.emp_no "
                        + "AND s.to_date = '9999-01-01' "
                        + "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no "
                        + "AND de.to_date = '9999-01-01' "
                        + "LEFT JOIN departments d ON de.dept_no = d.dept_no "
                        + "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no "
                        + "AND dm.to_date = '9999-01-01' "
                        + "LEFT JOIN employees manager ON dm.emp_no = manager.emp_no "
                        + "WHERE e.emp_no = ?";

        if (con == null) {
            System.out.println(
                    "Cannot get employee details: not connected to database");
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

                String deptNo = result.getString("dept_no");
                String deptName = result.getString("dept_name");

                if (deptNo != null) {
                    Department dept = new Department();
                    dept.dept_no = deptNo;
                    dept.dept_name = deptName;

                    int managerId = result.getInt("manager_emp_no");

                    if (!result.wasNull()) {
                        Employee manager = new Employee();
                        manager.emp_no = managerId;
                        manager.first_name =
                                result.getString("manager_first_name");
                        manager.last_name =
                                result.getString("manager_last_name");

                        dept.manager = manager;
                    }

                    employee.dept = dept;
                }

                return employee;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public Employee getEmployee(String first_name, String last_name) {
        String query =
                "SELECT emp_no FROM employees "
                        + "WHERE first_name = ? AND last_name = ? "
                        + "ORDER BY emp_no ASC LIMIT 1";

        if (con == null) {
            System.out.println(
                    "Cannot get employee details: not connected to database");
            return null;
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int employeeId = result.getInt("emp_no");
                    return getEmployee(employeeId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
        }

        return null;
    }


    public Department getDepartment(String dept_name) {
        String query =
                "SELECT d.dept_no, d.dept_name, "
                        + "e.emp_no AS manager_emp_no, "
                        + "e.first_name AS manager_first_name, "
                        + "e.last_name AS manager_last_name "
                        + "FROM departments d "
                        + "LEFT JOIN dept_manager dm "
                        + "ON d.dept_no = dm.dept_no "
                        + "AND dm.to_date = '9999-01-01' "
                        + "LEFT JOIN employees e "
                        + "ON dm.emp_no = e.emp_no "
                        + "WHERE d.dept_name = ?";

        if (con == null) {
            System.out.println(
                    "Cannot get department: not connected to database");
            return null;
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, dept_name);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Department dept = new Department();

                    dept.dept_no = result.getString("dept_no");
                    dept.dept_name = result.getString("dept_name");

                    int managerId = result.getInt("manager_emp_no");

                    if (!result.wasNull()) {
                        Employee manager = new Employee();

                        manager.emp_no = managerId;
                        manager.first_name =
                                result.getString("manager_first_name");
                        manager.last_name =
                                result.getString("manager_last_name");

                        dept.manager = manager;
                    }

                    return dept;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get department");
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        ArrayList<Employee> employees = new ArrayList<>();

        if (dept == null) {
            System.out.println("Department not found.");
            return employees;
        }

        String query =
                "SELECT employees.emp_no, employees.first_name, "
                        + "employees.last_name, salaries.salary "
                        + "FROM employees, salaries, dept_emp, departments "
                        + "WHERE employees.emp_no = salaries.emp_no "
                        + "AND employees.emp_no = dept_emp.emp_no "
                        + "AND dept_emp.dept_no = departments.dept_no "
                        + "AND salaries.to_date = '9999-01-01' "
                        + "AND dept_emp.to_date = '9999-01-01' "
                        + "AND departments.dept_no = ? "
                        + "ORDER BY employees.emp_no ASC";

        if (con == null) {
            System.out.println("Cannot retrieve salaries: not connected to database");
            return employees;
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, dept.dept_no);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Employee employee = new Employee();
                    employee.emp_no = result.getInt("emp_no");
                    employee.first_name = result.getString("first_name");
                    employee.last_name = result.getString("last_name");
                    employee.salary = result.getInt("salary");
                    employee.dept = dept;

                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve department salaries");
            System.out.println(e.getMessage());
        }

        return employees;
    }

    public ArrayList<Employee> getAllSalaries() {
        ArrayList<Employee> employees = new ArrayList<>();

        String query =
                "SELECT employees.emp_no, employees.first_name, "
                        + "employees.last_name, salaries.salary "
                        + "FROM employees, salaries "
                        + "WHERE employees.emp_no = salaries.emp_no "
                        + "AND salaries.to_date = '9999-01-01' "
                        + "ORDER BY employees.emp_no ASC";

        if (con == null) {
            System.out.println("Cannot get salaries: not connected to database");
            return employees;
        }

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Employee employee = new Employee();
                employee.emp_no = result.getInt("emp_no");
                employee.first_name = result.getString("first_name");
                employee.last_name = result.getString("last_name");
                employee.salary = result.getInt("salary");

                employees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all salaries");
        }

        return employees;
    }

    public void printSalaries(ArrayList<Employee> employees) {
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-20s %10s%n",
                "Emp No", "First Name", "Last Name", "Salary");
        System.out.println("------------------------------------------------------------");

        int limit = Math.min(employees.size(), 20);

        for (int i = 0; i < limit; i++) {
            Employee employee = employees.get(i);

            System.out.printf("%-10d %-20s %-20s %10d%n",
                    employee.emp_no,
                    employee.first_name,
                    employee.last_name,
                    employee.salary);
        }

        if (employees.size() > limit) {
            System.out.println("Showing the first " + limit + " employees.");
        }

        System.out.println("Total employees: " + employees.size());
        System.out.println("------------------------------------------------------------");
    }

    public void displayEmployee(Employee employee) {
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        String departmentName = employee.dept == null
                ? "Department: Unknown"
                : "Department: " + employee.dept.dept_name;

        String managerName;

        if (employee.dept == null || employee.dept.manager == null) {
            managerName = "Manager: Unknown";
        } else {
            Employee manager = employee.dept.manager;
            managerName = "Manager: "
                    + manager.first_name + " " + manager.last_name;
        }

        System.out.println(
                employee.emp_no + " "
                        + employee.first_name + " "
                        + employee.last_name + "\n"
                        + employee.title + "\n"
                        + "Salary: " + employee.salary + "\n"
                        + departmentName + "\n"
                        + managerName + "\n");
    }


    public void getSalariesByRole(String title) {
        String query =
                "SELECT employees.emp_no, employees.first_name, "
                        + "employees.last_name, salaries.salary "
                        + "FROM employees, salaries, titles "
                        + "WHERE employees.emp_no = salaries.emp_no "
                        + "AND employees.emp_no = titles.emp_no "
                        + "AND salaries.to_date = '9999-01-01' "
                        + "AND titles.to_date = '9999-01-01' "
                        + "AND titles.title = ? "
                        + "ORDER BY employees.emp_no ASC";

        if (con == null) {
            System.out.println("Cannot retrieve salaries: not connected to database");
            return;
        }

        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, title);

            try (ResultSet result = statement.executeQuery()) {
                System.out.println("\nSalary report for role: " + title);
                System.out.println("----------------------------------------");

                boolean found = false;

                while (result.next()) {
                    found = true;

                    System.out.println(
                            result.getInt("emp_no") + " "
                                    + result.getString("first_name") + " "
                                    + result.getString("last_name")
                                    + " | Salary: "
                                    + result.getInt("salary"));
                }

                if (!found) {
                    System.out.println("No employees found for role: " + title);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve salary report");
            System.out.println(e.getMessage());
        }
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
        app.connect();


        if (app.con == null) {
            System.err.println("Could not establish database connection");
            System.exit(1);
        }

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Choose an option:");
            System.out.println("1. View employee details by ID");
            System.out.println("2. Salary report by role");
            System.out.println("3. All current salaries");
            System.out.println("4. Salary report by department");
            System.out.println("5. Find employee by name");

            String choice = args.length > 0 ? args[0] : scanner.nextLine().trim();

            if ("2".equals(choice)) {
                String title;

                if (args.length > 1) {
                    title = args[1].trim();
                } else {
                    System.out.print("Enter job role (for example, Engineer): ");
                    title = scanner.nextLine().trim();
                }

                if (title.isEmpty()) {
                    System.out.println("Role cannot be empty.");
                } else {
                    app.getSalariesByRole(title);
                }

            } else if ("3".equals(choice)) {
                ArrayList<Employee> employees = app.getAllSalaries();
                System.out.println("Employees retrieved: " + employees.size());
                app.printSalaries(employees);

            } else if ("4".equals(choice)) {
                String deptName;

                if (args.length > 1) {
                    deptName = args[1].trim();
                } else {
                    System.out.print("Enter department name (for example, Sales): ");
                    deptName = scanner.nextLine().trim();
                }

                if (deptName.isEmpty()) {
                    System.out.println("Department name cannot be empty.");
                } else {
                    Department dept = app.getDepartment(deptName);

                    if (dept != null) {
                        System.out.println(
                                "\nSalary report for department: " + dept.dept_name);

                        ArrayList<Employee> employees =
                                app.getSalariesByDepartment(dept);

                        app.printSalaries(employees);
                    } else {
                        System.out.println("Department not found: " + deptName);
                    }
                }

            } else if ("1".equals(choice) || choice.isEmpty()) {
                int employeeId;

                if (args.length > 1) {
                    employeeId = Integer.parseInt(args[1].trim());
                } else {
                    System.out.print("Enter employee ID (default 255530): ");
                    String input = scanner.nextLine().trim();
                    employeeId = input.isEmpty()
                            ? 255530
                            : Integer.parseInt(input);
                }

                Employee employee = app.getEmployee(employeeId);
                app.displayEmployee(employee);

            } else if ("5".equals(choice)) {
                String firstName;
                String lastName;

                if (args.length > 2) {
                    firstName = args[1].trim();
                    lastName = args[2].trim();
                } else {
                    System.out.print("Enter first name: ");
                    firstName = scanner.nextLine().trim();

                    System.out.print("Enter last name: ");
                    lastName = scanner.nextLine().trim();
                }

                if (firstName.isEmpty() || lastName.isEmpty()) {
                    System.out.println("First name and last name cannot be empty.");
                } else {
                    Employee foundEmployee = app.getEmployee(firstName, lastName);

                    if (foundEmployee != null) {
                        app.displayEmployee(foundEmployee);
                    } else {
                        System.out.println("Employee not found.");
                    }
                }


            } else {
                System.out.println("Invalid menu option: " + choice);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid employee ID.");
        } finally {
            app.disconnect();
        }
    }
}