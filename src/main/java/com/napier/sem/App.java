package com.napier.sem;

import java.sql.*;

public class App
{
    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        Connection con = null;
        int retries = 100;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database... (Attempt " + (i + 1) + ")");
            try
            {
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
                System.out.println("Connected to database successfully!");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database. Waiting before retry...");
                sqle.printStackTrace();

                try
                {
                    Thread.sleep(10000);
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted");
                    break;
                }
            }
        }

        if (con != null)
        {
            try
            {
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT emp_no, first_name, last_name FROM employees LIMIT 5")) {

                    System.out.println("--- Sample Employees ---");
                    while (rs.next()) {
                        int empNo = rs.getInt("emp_no");
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        System.out.println(empNo + ": " + firstName + " " + lastName);
                    }
                }

                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error during database operation: " + e.getMessage());
            }
        }

        try
        {
            Thread.currentThread().join();
        }
        catch (InterruptedException e)
        {
            System.out.println("Main thread interrupted");
        }
    }
}