package org.example.emergency_connect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseOperations {

    public static void initializeDatabase(Connection conn) throws SQLException {
        try {
            // Check if table exists
            boolean tableExists = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "disaster_reports", null)) {
                tableExists = rs.next();
            }

            if (!tableExists) {
                // Create table if it doesn't exist
                String createTableSQL = """
                    CREATE TABLE disaster_reports (
                        request_id INT PRIMARY KEY,
                        full_name VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        contact VARCHAR(20) NOT NULL,
                        nic VARCHAR(20) NOT NULL,
                        address VARCHAR(200) NOT NULL,
                        grama_niladhari VARCHAR(100) NOT NULL,
                        district VARCHAR(50) NOT NULL,
                        province VARCHAR(50) NOT NULL,
                        disaster_type VARCHAR(50) NOT NULL,
                        report_date DATE NOT NULL,
                        impact_description TEXT NOT NULL,
                        number_of_affected INT NOT NULL,
                        urgency_level VARCHAR(20) NOT NULL,
                        status VARCHAR(20) DEFAULT 'Pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """;

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                    System.out.println("Database table created successfully");
                }
            } else {
                // Check if status column exists
                boolean statusColumnExists = false;
                try (ResultSet rs = conn.getMetaData().getColumns(null, null, "disaster_reports", "status")) {
                    statusColumnExists = rs.next();
                }

                // Add status column if it doesn't exist
                if (!statusColumnExists) {
                    String addStatusColumnSQL =
                            "ALTER TABLE disaster_reports ADD COLUMN status VARCHAR(20) DEFAULT 'Pending'";
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(addStatusColumnSQL);
                        System.out.println("Status column added successfully");
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw e;
        }
    }

    private static int getNextRequestId(Connection conn) throws SQLException {
        String sql = "SELECT request_id FROM disaster_reports ORDER BY request_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int expectedId = 1;
            while (rs.next()) {
                int currentId = rs.getInt("request_id");
                if (currentId != expectedId) {
                    return expectedId;
                }
                expectedId++;
            }
            return expectedId;
        }
    }

    public static boolean saveDisasterReport(DisasterReport report) {
        String sql = """
            INSERT INTO disaster_reports (
                request_id, full_name, email, contact, nic, address, grama_niladhari,
                district, province, disaster_type, report_date,
                impact_description, number_of_affected, urgency_level, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int nextId = getNextRequestId(conn);

            pstmt.setInt(1, nextId);
            pstmt.setString(2, report.getFullName());
            pstmt.setString(3, report.getEmail());
            pstmt.setString(4, report.getContact());
            pstmt.setString(5, report.getNic());
            pstmt.setString(6, report.getAddress());
            pstmt.setString(7, report.getGramaNiladhari());
            pstmt.setString(8, report.getDistrict());
            pstmt.setString(9, report.getProvince());
            pstmt.setString(10, report.getDisasterType());
            pstmt.setDate(11, Date.valueOf(report.getDate()));
            pstmt.setString(12, report.getImpactDescription());
            pstmt.setInt(13, report.getNumberOfAffected());
            pstmt.setString(14, report.getUrgencyLevel());
            pstmt.setString(15, "Pending"); // Default status for new reports

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to save report: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateReportStatus(int requestId, String newStatus) {
        String sql = "UPDATE disaster_reports SET status = ? WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, requestId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<DisasterReport> getAllReports() {
        ObservableList<DisasterReport> reports = FXCollections.observableArrayList();
        String sql = "SELECT * FROM disaster_reports ORDER BY request_id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DisasterReport report = new DisasterReport(
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("nic"),
                        rs.getString("address"),
                        rs.getString("grama_niladhari"),
                        rs.getString("district"),
                        rs.getString("province"),
                        rs.getString("disaster_type"),
                        rs.getDate("report_date").toLocalDate(),
                        rs.getString("impact_description"),
                        rs.getInt("number_of_affected"),
                        rs.getString("urgency_level")
                );
                report.setRequestId(rs.getInt("request_id"));
                report.setStatus(rs.getString("status"));
                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to load reports: " + e.getMessage());
        }

        return reports;
    }

    private static void showError(String title, String content) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}