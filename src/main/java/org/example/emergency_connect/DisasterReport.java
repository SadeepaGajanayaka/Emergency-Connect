// DisasterReport.java
package org.example.emergency_connect;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DisasterReport {
    private int requestId;
    private String fullName;
    private String email;
    private String contact;
    private String nic;
    private String address;
    private String gramaNiladhari;
    private String district;
    private String province;
    private String disasterType;
    private LocalDate date;
    private String impactDescription;
    private int numberOfAffected;
    private String urgencyLevel;
    private LocalDateTime createdAt;  // Added for timestamp
    private String status;

    // Constructor
    public DisasterReport(String fullName, String email, String contact, String nic,
                          String address, String gramaNiladhari, String district,
                          String province, String disasterType, LocalDate date,
                          String impactDescription, int numberOfAffected, String urgencyLevel) {
        this.fullName = fullName;
        this.email = email;
        this.contact = contact;
        this.nic = nic;
        this.address = address;
        this.gramaNiladhari = gramaNiladhari;
        this.district = district;
        this.province = province;
        this.disasterType = disasterType;
        this.date = date;
        this.impactDescription = impactDescription;
        this.numberOfAffected = numberOfAffected;
        this.urgencyLevel = urgencyLevel;
        this.createdAt = LocalDateTime.now();
        this.status = "Pending"; //Default status
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGramaNiladhari() {
        return gramaNiladhari;
    }

    public void setGramaNiladhari(String gramaNiladhari) {
        this.gramaNiladhari = gramaNiladhari;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImpactDescription() {
        return impactDescription;
    }

    public void setImpactDescription(String impactDescription) {
        this.impactDescription = impactDescription;
    }

    public int getNumberOfAffected() {
        return numberOfAffected;
    }

    public void setNumberOfAffected(int numberOfAffected) {
        this.numberOfAffected = numberOfAffected;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }
}