package org.example.emergency_connect;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class DisasterInfoController {

    @FXML private ComboBox<String> disasterTypeCombo;
    @FXML private ImageView closeButton;
    @FXML private ImageView closeButton1;
    @FXML private AnchorPane rootPane;
    @FXML private Button back_btn;
    @FXML private Button submit_btn;
    @FXML private TextField address_textfield;
    @FXML private DatePicker datePicker;
    @FXML private TextArea impactDescriptionField;
//    @FXML private ComboBox<String> districtCombo;
    @FXML private ComboBox<String> urgencyCombo;
    @FXML private VBox complaintsSection;
    @FXML private VBox adminSection;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Initialize ComboBox values
        if (disasterTypeCombo != null) {
            disasterTypeCombo.getItems().addAll("Flood", "Landslide", "Earthquake", "Tsunami", "Cyclone");
        }

        if (urgencyCombo != null) {
            urgencyCombo.getItems().addAll("Low", "Medium", "High", "Critical");
        }

        String cssPath = getClass().getResource("/org/example/emergency_connect/styles/style.css").toExternalForm();
        rootPane.getStylesheets().add(cssPath);

        setupEventHandlers();


        loadSavedData();

        // Add listeners for real-time validation
        setupValidationListeners();

        // Apply CSS classes
        applyStyles();
    }

    private void applyStyles() {
        if (address_textfield != null) {
            address_textfield.getStyleClass().add("text-field");
        }
        if (impactDescriptionField != null) {
            impactDescriptionField.getStyleClass().add("text-area");
        }
        if (disasterTypeCombo != null) {
            disasterTypeCombo.getStyleClass().add("combo-box");
        }
        if (urgencyCombo != null) {
            urgencyCombo.getStyleClass().add("combo-box");
        }
        if (datePicker != null) {
            datePicker.getStyleClass().add("date-picker");
        }
        if (back_btn != null) {
            back_btn.getStyleClass().add("button");
        }
        if (submit_btn != null) {
            submit_btn.getStyleClass().add("button");
        }
    }

    private void setupEventHandlers() {
        if (back_btn != null) {
            back_btn.setOnMouseClicked(this::handleBackButtonClick);
        }
        if (closeButton != null) {
            closeButton.setOnMouseClicked(this::handleCloseButtonClick);
        }
        if (submit_btn != null) {
            submit_btn.setOnMouseClicked(this::handleSubmitButtonClick);
        }
        if (complaintsSection != null) {
            complaintsSection.setOnMouseClicked(this::handleComplaintsClick);
        }
        if (adminSection != null) {
            adminSection.setOnMouseClicked(this::handleAdminClick);
        }
        if (closeButton1 != null) {
            closeButton1.setOnMouseClicked(event -> minimizeWindow());
        }


        // Make window draggable
        if (rootPane != null) {
            rootPane.setOnMousePressed(this::handleMousePressed);
            rootPane.setOnMouseDragged(this::handleMouseDragged);
        }
    }
    private void minimizeWindow() {
        Stage stage = (Stage) closeButton1.getScene().getWindow();
        stage.setIconified(true);
    }
    private void setupValidationListeners() {
        if (address_textfield != null) {
            address_textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) { // When focus is lost
                    ValidationUtil.validateTextField(address_textfield, "Number of affected individuals");
                }
            });
        }

        if (impactDescriptionField != null) {
            impactDescriptionField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    ValidationUtil.validateTextArea(impactDescriptionField, "Impact description");
                }
            });
        }
    }

    private void handleComplaintsClick(MouseEvent event) {
        saveFormData();
        loadNextScene("Complaints.fxml", "Complaints Section");
    }

    private void handleAdminClick(MouseEvent event) {
        saveFormData();
        loadNextScene("Admin.fxml", "Admin Section");
    }

    private void loadSavedData() {
        FormData formData = FormData.getInstance();
        if (formData.getNumberOfAffectedIndividuals() != null) {
            address_textfield.setText(formData.getNumberOfAffectedIndividuals());
        }
        if (formData.getDate() != null) {
            datePicker.setValue(formData.getDate());
        }
        if (formData.getImpactDescription() != null) {
            impactDescriptionField.setText(formData.getImpactDescription());
        }
        if (formData.getDisasterType() != null) {
            disasterTypeCombo.setValue(formData.getDisasterType());
        }
        if (formData.getUrgencyLevel() != null) {
            urgencyCombo.setValue(formData.getUrgencyLevel());
        }
    }

    private void handleSubmitButtonClick(MouseEvent event) {
        if (validateAllFields()) {
            saveFormData();

            // Submit the report
            boolean submitted = DisasterReportSubmissionHandler.submitDisasterReport();

            if (submitted) {
                clearAllFormData(); // Clear both forms' data
                showAlert("Success", "Report submitted successfully!");

                // Navigate back to complaints section
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/Complaints.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Navigation Error", "Error returning to complaints form: " + e.getMessage());
                }
            } else {
                showAlert("Error", "Failed to submit report. Please try again.");
            }
        } else {
            showAlert("Validation Error", "Please fill all required fields correctly.");
        }
    }

    private void clearForm() {
        address_textfield.clear();
        datePicker.setValue(null);
        impactDescriptionField.clear();
        disasterTypeCombo.setValue(null);
        urgencyCombo.setValue(null);
    }
    private boolean validateAllFields() {
        boolean isValid = true;

        isValid &= ValidationUtil.validateTextField(address_textfield, "Number of affected individuals");
        isValid &= ValidationUtil.validateTextArea(impactDescriptionField, "Impact description");
        isValid &= ValidationUtil.validateComboBox(disasterTypeCombo, "Disaster type");
        isValid &= ValidationUtil.validateComboBox(urgencyCombo, "Urgency level");
        isValid &= ValidationUtil.validateDatePicker(datePicker, "Date");

        return isValid;
    }

    private void saveFormData() {
        FormData formData = FormData.getInstance();
        formData.setNumberOfAffectedIndividuals(address_textfield.getText());
        formData.setDate(datePicker.getValue());
        formData.setImpactDescription(impactDescriptionField.getText());
        formData.setDisasterType(disasterTypeCombo.getValue());
        formData.setUrgencyLevel(urgencyCombo.getValue());
    }

    private void handleBackButtonClick(MouseEvent event) {
        saveFormData();
        loadNextScene("Complaints.fxml", "Complaints Section");
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

    private void loadNextScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/emergency_connect/" + fxmlFile));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
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
    private void clearAllFormData() {
        // Clear current form data
        FormData disasterData = FormData.getInstance();
        disasterData.setDisasterType(null);
        disasterData.setDate(null);
        disasterData.setImpactDescription(null);
        disasterData.setUrgencyLevel(null);
        disasterData.setNumberOfAffectedIndividuals(null);

        // Clear complaints form data
        ComplaintsFormData complaintsData = ComplaintsFormData.getInstance();
        complaintsData.setFullName(null);
        complaintsData.setEmail(null);
        complaintsData.setContactNumber(null);
        complaintsData.setNicNumber(null);
        complaintsData.setAddress(null);
        complaintsData.setGramaNiladhariDivision(null);
        complaintsData.setDistrict(null);
        complaintsData.setProvince(null);
    }
}