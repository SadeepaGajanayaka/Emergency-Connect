package org.example.emergency_connect;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Admin {
    @FXML private TableView<DisasterReport> reportTable;
    @FXML private TableColumn<DisasterReport, Integer> requestIdCol;
    @FXML private TableColumn<DisasterReport, String> fullNameCol;
    @FXML private TableColumn<DisasterReport, String> emailCol;
    @FXML private TableColumn<DisasterReport, String> contactCol;
    @FXML private TableColumn<DisasterReport, String> nicCol;
    @FXML private TableColumn<DisasterReport, String> addressCol;
    @FXML private TableColumn<DisasterReport, String> gramaNiladhariCol;
    @FXML private TableColumn<DisasterReport, String> districtCol;
    @FXML private TableColumn<DisasterReport, String> provinceCol;
    @FXML private TableColumn<DisasterReport, String> disasterTypeCol;
    @FXML private TableColumn<DisasterReport, LocalDate> dateCol;
    @FXML private TableColumn<DisasterReport, String> impactCol;
    @FXML private TableColumn<DisasterReport, Integer> affectedCol;
    @FXML private TableColumn<DisasterReport, String> urgencyCol;

    @FXML private TextField fullname_text;
    @FXML private TextField email_text;
    @FXML private TextField contact_text;
    @FXML private TextField NIC_Number_text;
    @FXML private TextField address_text;
    @FXML private TextField gramaniladhari_text;
    @FXML private TextField district_text;
    @FXML private TextField province_text;
    @FXML private TextField disastertype_text;
    @FXML private TextField date_text;
    @FXML private TextField affected_text;
    @FXML private TextField urgencylevel_text;
    @FXML private TextArea impactDescription_text;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button update_btn;
    @FXML private TableColumn<DisasterReport, String> statusCol;

    @FXML private ImageView closeButton;
    @FXML private ImageView closeButton1;
    @FXML private AnchorPane rootPane;
    @FXML private VBox adminSection;
    @FXML private VBox complaintsSection;
    @FXML private VBox dashboardSection;

    @FXML private Button clear_btn;
    @FXML private Button delete_btn;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        try {
            // Initialize UI components
            setupEventHandlers();
            setupTableColumns();
            setupTableSelectionListener();


            //
            statusCombo.getItems().addAll("Pending", "In Progress", "Resolved");
            // Make other fields not editable
            fullname_text.setEditable(false);
            email_text.setEditable(false);
            contact_text.setEditable(false);
            NIC_Number_text.setEditable(false);
            address_text.setEditable(false);
            gramaniladhari_text.setEditable(false);
            district_text.setEditable(false);
            province_text.setEditable(false);
            disastertype_text.setEditable(false);
            date_text.setEditable(false);
            affected_text.setEditable(false);
            urgencylevel_text.setEditable(false);
            impactDescription_text.setEditable(false);

            loadTableData();
            // Register for updates
            UpdateNotifier.registerAdminView(this);

            // Test database connection
            if (!DatabaseConnection.testConnection()) {
                showError("Database Error", "Could not establish database connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Initialization Error", "Failed to initialize Admin view: " + e.getMessage());
        }
    }

    public void refreshTable() {
        loadTableData();
    }
    private void setupTableSelectionListener() {
        reportTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFormFields(newSelection);
            }
        });
    }
    private void populateFormFields(DisasterReport report) {
        fullname_text.setText(report.getFullName());
        email_text.setText(report.getEmail());
        contact_text.setText(report.getContact());
        NIC_Number_text.setText(report.getNic());
        address_text.setText(report.getAddress());
        gramaniladhari_text.setText(report.getGramaNiladhari());
        district_text.setText(report.getDistrict());
        province_text.setText(report.getProvince());
        disastertype_text.setText(report.getDisasterType());
        date_text.setText(report.getDate().toString());
        affected_text.setText(String.valueOf(report.getNumberOfAffected()));
        urgencylevel_text.setText(report.getUrgencyLevel());
        impactDescription_text.setText(report.getImpactDescription());
        statusCombo.setValue(report.getStatus());
    }
    private void clearFormFields() {
        fullname_text.clear();
        email_text.clear();
        contact_text.clear();
        NIC_Number_text.clear();
        address_text.clear();
        gramaniladhari_text.clear();
        district_text.clear();
        province_text.clear();
        disastertype_text.clear();
        date_text.clear();
        affected_text.clear();
        urgencylevel_text.clear();
        impactDescription_text.clear();
        reportTable.getSelectionModel().clearSelection();
    }
    private void deleteSelectedRecord() {
        DisasterReport selectedReport = reportTable.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            showAlert("Selection Error", "Please select a record to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this record?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                if (deleteReport(selectedReport.getRequestId())) {
                    loadTableData();
                    clearFormFields();
                    showAlert("Success", "Record deleted successfully");
                }
            } catch (SQLException e) {
                showError("Delete Error", "Failed to delete record: " + e.getMessage());
            }
        }
    }

    private boolean deleteReport(int requestId) throws SQLException {
        String sql = "DELETE FROM disaster_reports WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    private void setupEventHandlers() {
        closeButton.setOnMouseClicked(this::handleCloseButtonClick);
        closeButton1.setOnMouseClicked(event -> minimizeWindow());
        adminSection.setOnMouseClicked(this::handleAdminSectionClick);
        complaintsSection.setOnMouseClicked(this::handleComplaintsSectionClick);
        dashboardSection.setOnMouseClicked(this::handleDashboardClick);
        //        clear_btn.setOnMouseClicked(this::handleClearButtonClick);
        clear_btn.setOnMouseClicked(event -> clearFormFields());
        delete_btn.setOnMouseClicked(event -> deleteSelectedRecord());
        update_btn.setOnAction(event -> handleUpdateStatus());

        VBox dashboardSection = (VBox) rootPane.lookup("VBox[layoutY='160']");
        if (dashboardSection != null) {
            dashboardSection.setOnMouseClicked(this::handleDashboardClick);
        }

        rootPane.setOnMousePressed(this::handleMousePressed);
        rootPane.setOnMouseDragged(this::handleMouseDragged);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void handleDashboardClick(MouseEvent event) {
        loadNextScene("DashboardController.fxml", "Dashboard");
    }
    private void setupTableColumns() {
        requestIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        nicCol.setCellValueFactory(new PropertyValueFactory<>("nic"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        gramaNiladhariCol.setCellValueFactory(new PropertyValueFactory<>("gramaNiladhari"));
        districtCol.setCellValueFactory(new PropertyValueFactory<>("district"));
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        disasterTypeCol.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        impactCol.setCellValueFactory(new PropertyValueFactory<>("impactDescription"));
        affectedCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAffected"));
        urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgencyLevel"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadTableData() {
        try {
            reportTable.setItems(DatabaseOperations.getAllReports());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Data Loading Error", "Failed to load disaster reports: " + e.getMessage());
        }
    }
    private void handleUpdateStatus() {
        DisasterReport selectedReport = reportTable.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            showAlert("Selection Error", "Please select a report to update");
            return;
        }

        String newStatus = statusCombo.getValue();
        if (newStatus == null || newStatus.isEmpty()) {
            showAlert("Input Error", "Please select a status");
            return;
        }

        if (DatabaseOperations.updateReportStatus(selectedReport.getRequestId(), newStatus)) {
            loadTableData();
            showAlert("Success", "Status updated successfully");
        } else {
            showError("Update Error", "Failed to update status");
        }
    }
    private void handleCloseButtonClick(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void minimizeWindow() {
        Stage stage = (Stage) closeButton1.getScene().getWindow();
        stage.setIconified(true);
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

    private void handleAdminSectionClick(MouseEvent event) {
        loadNextScene("Admin.fxml", "Admin Section");
    }

    private void handleComplaintsSectionClick(MouseEvent event) {
        loadNextScene("Complaints.fxml", "Complaints Section");
    }

    private void handleClearButtonClick(MouseEvent event) {
        reportTable.getItems().clear();
        loadTableData();
    }

    private void loadNextScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) adminSection.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Failed to load " + title + ": " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}