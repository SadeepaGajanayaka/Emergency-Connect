package org.example.emergency_connect;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardController implements DashboardView {

    @FXML private VBox complaintsSection;
    @FXML private VBox adminSection;
    @FXML private ImageView closeButton;
    @FXML private AnchorPane rootPane;
    @FXML private PieChart Piechartbydisastertype;
    @FXML private BarChart<String, Number> bargraphbystatus;
    @FXML private Label totalDisastersLabel;
    @FXML private ImageView closeButton1;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        setupEventHandlers();
        UpdateNotifier.registerDashboardView(this);
        refreshDashboard();
    }

    private void setupEventHandlers() {
        complaintsSection.setOnMouseClicked(this::handleComplaintsClick);
        adminSection.setOnMouseClicked(this::handleAdminClick);
        closeButton.setOnMouseClicked(this::handleCloseButtonClick);
        rootPane.setOnMousePressed(this::handleMousePressed);
        rootPane.setOnMouseDragged(this::handleMouseDragged);
        if (closeButton1 != null) {
            closeButton1.setOnMouseClicked(event -> minimizeWindow());
        }
    }

    @Override
    public void refreshDashboard() {
        updateTotalDisasters();
        updatePieChart();
        updateBarGraph();
    }

    private void updateTotalDisasters() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM disaster_reports")) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt(1);
                totalDisastersLabel.setText(String.valueOf(total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT disaster_type, COUNT(*) as count FROM disaster_reports GROUP BY disaster_type")) {

            ResultSet rs = stmt.executeQuery();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (rs.next()) {
                String disasterType = rs.getString("disaster_type");
                int count = rs.getInt("count");
                pieChartData.add(new PieChart.Data(disasterType, count));
            }

            Piechartbydisastertype.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBarGraph() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT status, COUNT(*) as count FROM disaster_reports GROUP BY status")) {

            ResultSet rs = stmt.executeQuery();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Reports by Status");

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(status, count));
            }
            String barColor = "#1B5E20";
            bargraphbystatus.getData().clear();
            bargraphbystatus.getData().add(series);

            // Style the bars and legend after the series has been added to the chart
            Platform.runLater(() -> {
                // Style the bars
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (data.getNode() != null) {
                        data.getNode().setStyle("-fx-bar-fill: " + barColor + ";");
                    }
                }

                // Apply CSS to ensure legend symbol is green
                bargraphbystatus.lookupAll(".chart-legend-item-symbol").forEach(
                        node -> node.setStyle("-fx-background-color: " + barColor + ";")
                );

                // Style the legend
                if (series.getNode() != null) {
                    series.getNode().setStyle("-fx-background-color: " + barColor + ", white;");
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void minimizeWindow() {
        Stage stage = (Stage) closeButton1.getScene().getWindow();
        stage.setIconified(true);
    }
    private void handleCloseButtonClick(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void handleComplaintsClick(MouseEvent event) {
        loadNextScene("Complaints.fxml", "Complaints Section");
    }

    private void handleAdminClick(MouseEvent event) {
        loadNextScene("Admin.fxml", "Admin Section");
    }

    private void loadNextScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) complaintsSection.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}