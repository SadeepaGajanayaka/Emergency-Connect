package org.example.emergency_connect;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Complaints {

    @FXML private VBox complaintsSection;
    @FXML private VBox adminSection;
    @FXML private VBox dashboardSection;

    @FXML private ImageView closeButton;
    @FXML private ImageView closeButton1;
    @FXML private AnchorPane rootPane;
    @FXML private Button next_btn;
    @FXML private Button clear_btn;

    // Add all the form fields
    @FXML private TextField fullname_textfield;
    @FXML private TextField email_textfield;
    @FXML private TextField contact_textfield;
    @FXML private TextField NIC_textfield;
    @FXML private TextField address_textfield;
    @FXML private TextField gramaniladhari_textfield;
    @FXML private ComboBox<String> district_combo;
    @FXML private ComboBox<String> province_combo;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Initialize ComboBoxes
        initializeComboBoxes();

        // Set up event handlers
        setupEventHandlers();

        // Load saved data
        loadSavedData();

        // Setup validation listeners
        setupValidationListeners();

    }


    private void initializeComboBoxes() {
        // Initialize district combo box
        district_combo.getItems().addAll(
                "Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya",
                "Galle", "Matara", "Hambantota", "Jaffna", "Kilinochchi", "Mannar",
                "Vavuniya", "Mullaitivu", "Batticaloa", "Ampara", "Trincomalee",
                "Kurunegala", "Puttalam", "Anuradhapura", "Polonnaruwa", "Badulla",
                "Monaragala", "Ratnapura", "Kegalle"
        );

        // Initialize province combo box
        province_combo.getItems().addAll(
                "Western", "Central", "Southern", "Northern", "Eastern",
                "North Western", "North Central", "Uva", "Sabaragamuwa"
        );

        String cssPath = getClass().getResource("/org/example/emergency_connect/styles/style.css").toExternalForm();
        rootPane.getStylesheets().add(cssPath);

    }

    private void setupEventHandlers() {
        complaintsSection.setOnMouseClicked(this::handleComplaintsClick);
        adminSection.setOnMouseClicked(this::handleAdminClick);
        closeButton.setOnMouseClicked(this::handleCloseButtonClick);
        next_btn.setOnAction(event -> handleNextButtonClick());
//        next_btn.setOnMouseClicked(this::handleNextButtonClick);
        clear_btn.setOnMouseClicked(this::handleClearButtonClick);
        dashboardSection.setOnMouseClicked(this::handleDashboardClick);

        VBox dashboardSection = (VBox) rootPane.lookup("VBox[layoutY='160']");
        if (dashboardSection != null) {
            dashboardSection.setOnMouseClicked(this::handleDashboardClick);
        }
        if (closeButton1 != null) {
            closeButton1.setOnMouseClicked(event -> minimizeWindow());
        }

        rootPane.setOnMousePressed(this::handleMousePressed);
        rootPane.setOnMouseDragged(this::handleMouseDragged);
    }
    private void minimizeWindow() {
        Stage stage = (Stage) closeButton1.getScene().getWindow();
        stage.setIconified(true);
    }
    private void handleDashboardClick(MouseEvent event) {
        loadNextScene("DashboardController.fxml", "Dashboard");
    }
    private void loadSavedData() {
        ComplaintsFormData formData = ComplaintsFormData.getInstance();

        if (formData.getFullName() != null) fullname_textfield.setText(formData.getFullName());
        if (formData.getEmail() != null) email_textfield.setText(formData.getEmail());
        if (formData.getContactNumber() != null) contact_textfield.setText(formData.getContactNumber());
        if (formData.getNicNumber() != null) NIC_textfield.setText(formData.getNicNumber());
        if (formData.getAddress() != null) address_textfield.setText(formData.getAddress());
        if (formData.getGramaNiladhariDivision() != null) gramaniladhari_textfield.setText(formData.getGramaNiladhariDivision());
        if (formData.getDistrict() != null) district_combo.setValue(formData.getDistrict());
        if (formData.getProvince() != null) province_combo.setValue(formData.getProvince());
    }

    private void setupValidationListeners() {
        fullname_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // When focus is lost
                ValidationUtil.validateTextField(fullname_textfield, "Full name");
            }
        });

        email_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                ValidationUtil.validateTextField(email_textfield, "Email");
            }
        });

        contact_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                ValidationUtil.validateTextField(contact_textfield, "Contact number");
            }
        });

        NIC_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                ValidationUtil.validateTextField(NIC_textfield, "NIC number");
            }
        });
    }

    private void saveFormData() {
        ComplaintsFormData formData = ComplaintsFormData.getInstance();
        formData.setFullName(fullname_textfield.getText());
        formData.setEmail(email_textfield.getText());
        formData.setContactNumber(contact_textfield.getText());
        formData.setNicNumber(NIC_textfield.getText());
        formData.setAddress(address_textfield.getText());
        formData.setGramaNiladhariDivision(gramaniladhari_textfield.getText());
        formData.setDistrict(district_combo.getValue());
        formData.setProvince(province_combo.getValue());
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        isValid &= ValidationUtil.validateTextField(fullname_textfield, "Full name");
        isValid &= ValidationUtil.validateTextField(email_textfield, "Email");
        isValid &= ValidationUtil.validateTextField(contact_textfield, "Contact number");
        isValid &= ValidationUtil.validateTextField(NIC_textfield, "NIC number");
        isValid &= ValidationUtil.validateTextField(address_textfield, "Address");
        isValid &= ValidationUtil.validateTextField(gramaniladhari_textfield, "Grama Niladhari Division");
        isValid &= ValidationUtil.validateComboBox(district_combo, "District");
        isValid &= ValidationUtil.validateComboBox(province_combo, "Province");

        return isValid;
    }


private void handleNextButtonClick() {
    if (validateAllFields()) {
        saveFormData();
        try {
            // Get the FXML loader
            FXMLLoader loader = new FXMLLoader();
            // Set the location explicitly
            loader.setLocation(getClass().getResource("/org/example/emergency_connect/DisasterInfo.fxml"));

            // Create new scene
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get current stage and set new scene
            Stage stage = (Stage) next_btn.getScene().getWindow();
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    } else {
        showAlert("Validation Error", "Please fill all required fields correctly.");
    }
}

    private void handleClearButtonClick(MouseEvent event) {
        fullname_textfield.clear();
        email_textfield.clear();
        contact_textfield.clear();
        NIC_textfield.clear();
        address_textfield.clear();
        gramaniladhari_textfield.clear();
        district_combo.setValue(null);
        province_combo.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        // Get the correct resource URL
        URL resourceUrl = getClass().getResource("/org/example/emergency_connect/" + fxmlFile);
        if (resourceUrl == null) {
            throw new IOException("FXML file not found: " + fxmlFile);
        }

        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Get the stage from any existing component
        Stage stage = (Stage) next_btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Navigation Error");
        alert.setHeaderText("Failed to load next screen");
        alert.setContentText("Error details: " + e.getMessage());
        alert.showAndWait();
    }
}
}