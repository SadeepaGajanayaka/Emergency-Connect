package org.example.emergency_connect;

import java.time.LocalDate;

public class FormData {
        private static FormData instance;

        // Fields to store user input data

        private String disasterType;
        private LocalDate date;
        private String impactDescription;
        private String urgencyLevel;
        private String numberOfAffectedIndividuals;

        private FormData() {}

        public static FormData getInstance() {
            if (instance == null) {
                instance = new FormData();
            }
            return instance;
        }

        // Getters and setters for the fields
        public String getDisasterType() {
            return disasterType;
        }

        public void setDisasterType(String disasterType) {
            this.disasterType = disasterType;
        }


    public LocalDate getDate() {  // Changed return type
        return date;
    }

    public void setDate(LocalDate date) {  // Changed parameter type
        this.date = date;
    }
        public String getImpactDescription() {
            return impactDescription;
        }

        public void setImpactDescription(String impactDescription) {
            this.impactDescription = impactDescription;
        }

        public String getUrgencyLevel() {
            return urgencyLevel;
        }

        public void setUrgencyLevel(String urgencyLevel) {
            this.urgencyLevel = urgencyLevel;
        }

        public String getNumberOfAffectedIndividuals() {
            return numberOfAffectedIndividuals;
        }

        public void setNumberOfAffectedIndividuals(String numberOfAffectedIndividuals) {
            this.numberOfAffectedIndividuals = numberOfAffectedIndividuals;
        }
    public void clearData() {
        this.disasterType = null;
        this.date = null;
        this.impactDescription = null;
        this.urgencyLevel = null;
        this.numberOfAffectedIndividuals = null;
    }
    }




