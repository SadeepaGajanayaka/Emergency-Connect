package org.example.emergency_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE = "disaster_coordination";
    private static final String USER = "root";
    private static final String PASSWORD = "Slg2398$&hd24114@#";

    private static Connection connection;
    private static boolean isInitialized = false;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the MySQL driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // First try to connect without database to create if needed
                try (Connection tempConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    try (Statement stmt = tempConn.createStatement()) {
                        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE);
                    }
                }

                // Now connect to the specific database
                connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);

                // Test the connection
                if (!connection.isValid(5)) {
                    throw new SQLException("Database connection test failed");
                }

                // Initialize database schema if not already done
                if (!isInitialized) {
                    DatabaseOperations.initializeDatabase(connection);
                    isInitialized = true;
                }

                return connection;

            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Please add MySQL Connector/J to your project.", e);
            } catch (SQLException e) {
                String errorMessage = "Database connection error: " + e.getMessage() +
                        "\nPlease check:\n" +
                        "1. MySQL server is running\n" +
                        "2. Username and password are correct\n" +
                        "3. MySQL server is accepting connections on port 3306";
                throw new SQLException(errorMessage, e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            } finally {
                connection = null;
                isInitialized = false;
            }
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && conn.isValid(5);
            if (isValid) {
                System.out.println("Database connection successful!");
            } else {
                System.err.println("Database connection failed validation check");
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}