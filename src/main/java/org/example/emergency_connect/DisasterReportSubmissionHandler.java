package org.example.emergency_connect;

import javafx.application.Platform;

public class DisasterReportSubmissionHandler {

    public static boolean submitDisasterReport() {
        try {
            // Get data from both forms
            ComplaintsFormData complaintsData = ComplaintsFormData.getInstance();
            FormData disasterData = FormData.getInstance();

            // Create a new disaster report
            DisasterReport report = new DisasterReport(
                    complaintsData.getFullName(),
                    complaintsData.getEmail(),
                    complaintsData.getContactNumber(),
                    complaintsData.getNicNumber(),
                    complaintsData.getAddress(),
                    complaintsData.getGramaNiladhariDivision(),
                    complaintsData.getDistrict(),
                    complaintsData.getProvince(),
                    disasterData.getDisasterType(),
                    disasterData.getDate(),
                    disasterData.getImpactDescription(),
                    Integer.parseInt(disasterData.getNumberOfAffectedIndividuals()),
                    disasterData.getUrgencyLevel()
            );

            // Save to database
            boolean saved = DatabaseOperations.saveDisasterReport(report);

            if (saved) {
                // Update all views
                Platform.runLater(() -> {
                    UpdateNotifier.notifyViews();
                });
            }

            return saved;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}