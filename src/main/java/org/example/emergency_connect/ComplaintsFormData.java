package org.example.emergency_connect;

import java.time.LocalDateTime;

public class ComplaintsFormData {


    private static ComplaintsFormData instance;

    private String fullName;
    private String email;
    private String contactNumber;
    private String nicNumber;
    private String address;
    private String gramaNiladhariDivision;
    private String district;
    private String province;
    private LocalDateTime lastUpdated;

    private ComplaintsFormData() {
        this.lastUpdated = LocalDateTime.now();
    }

    public static ComplaintsFormData getInstance() {
        if (instance == null) {
            instance = new ComplaintsFormData();
        }
        return instance;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }


public void setFullName(String fullName) {
    this.fullName = fullName;
    this.lastUpdated = LocalDateTime.now();
}


    public String getEmail() {
        return email;
    }


public void setEmail(String email) {
    this.email = email;
    this.lastUpdated = LocalDateTime.now();
}
    public String getContactNumber() {
        return contactNumber;
    }


public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
    this.lastUpdated = LocalDateTime.now();
}

    public String getNicNumber() {
        return nicNumber;
    }


public void setNicNumber(String nicNumber) {
    this.nicNumber = nicNumber;
    this.lastUpdated = LocalDateTime.now();
}


    public String getAddress() {
        return address;
    }


public void setAddress(String address) {
    this.address = address;
    this.lastUpdated = LocalDateTime.now();
}


    public String getGramaNiladhariDivision() {
        return gramaNiladhariDivision;
    }


public void setGramaNiladhariDivision(String gramaNiladhariDivision) {
    this.gramaNiladhariDivision = gramaNiladhariDivision;
    this.lastUpdated = LocalDateTime.now();
}
    public String getDistrict() {
        return district;
    }


public void setDistrict(String district) {
    this.district = district;
    this.lastUpdated = LocalDateTime.now();
}

    public String getProvince() {
        return province;
    }


public void setProvince(String province) {
    this.province = province;
    this.lastUpdated = LocalDateTime.now();
}
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void clearData() {
        this.fullName = null;
        this.email = null;
        this.contactNumber = null;
        this.nicNumber = null;
        this.address = null;
        this.gramaNiladhariDivision = null;
        this.district = null;
        this.province = null;
        this.lastUpdated = LocalDateTime.now();
    }
}