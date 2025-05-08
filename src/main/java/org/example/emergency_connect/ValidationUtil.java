
package org.example.emergency_connect;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;

public class ValidationUtil {

    private static final String ERROR_STYLE = "-fx-border-color: red;";
    private static final String NORMAL_STYLE = "-fx-border-color: #808080;";

    public static boolean validateTextField(TextField field, String fieldName) {
        String value = field.getText().trim();

        if (value.isEmpty()) {
            field.setStyle(ERROR_STYLE);
            field.setPromptText(fieldName + " is required");
            return false;
        }

        // Specific validations based on field type
        boolean isValid = true;
        String promptText = "";

        switch (fieldName.toLowerCase()) {
            case "contact number":
                if (!value.matches("\\d{10}")) {
                    isValid = false;
                    promptText = "Enter valid 10-digit number";
                }
                break;

            case "nic number":
                if (!value.matches("\\d{9}[vVxX]|\\d{12}")) {
                    isValid = false;
                    promptText = "Enter valid NIC number";
                }
                break;

            case "email":
                if (!value.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                    isValid = false;
                    promptText = "Enter valid email address";
                }
                break;

            case "number of affected individuals":
                if (!value.matches("\\d+")) {
                    isValid = false;
                    promptText = "Enter valid number";
                }
                break;
        }

        if (!isValid) {
            field.setStyle(ERROR_STYLE);
            field.setPromptText(promptText);
            return false;
        }

        field.setStyle(NORMAL_STYLE);
        field.setPromptText("");
        return true;
    }

    public static boolean validateTextArea(TextArea area, String fieldName) {
        String value = area.getText().trim();

        if (value.isEmpty()) {
            area.setStyle(ERROR_STYLE);
            area.setPromptText(fieldName + " is required");
            return false;
        }

        area.setStyle(NORMAL_STYLE);
        area.setPromptText("");
        return true;
    }

    public static boolean validateComboBox(ComboBox<?> comboBox, String fieldName) {
        if (comboBox.getValue() == null) {
            comboBox.setStyle(ERROR_STYLE);
            comboBox.setPromptText("Select " + fieldName);
            return false;
        }

        comboBox.setStyle(NORMAL_STYLE);
        comboBox.setPromptText("");
        return true;
    }

    public static boolean validateDatePicker(DatePicker datePicker, String fieldName) {
        if (datePicker.getValue() == null) {
            datePicker.setStyle(ERROR_STYLE);
            datePicker.setPromptText("Select " + fieldName);
            return false;
        }

        datePicker.setStyle(NORMAL_STYLE);
        datePicker.setPromptText("");
        return true;
    }
}