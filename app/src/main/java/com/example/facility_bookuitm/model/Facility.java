package com.example.facility_bookuitm.model;

public class Facility
{
    private int facilityID;
    private String facilityName;
    private String facilityLocation;
    private String facilityPicture;
    private String facilityStatus;
    private String facilityType;
    private int facilityCapacity;

    private User user;

    public Facility() {
    }

    public Facility(int facilityID, String facilityName, String facilityLocation, String facilityPicture, String facilityStatus, String facilityType, int facilityCapacity) {
        this.facilityID = facilityID;
        this.facilityName = facilityName;
        this.facilityLocation = facilityLocation;
        this.facilityPicture = facilityPicture;
        this.facilityStatus = facilityStatus;
        this.facilityType = facilityType;
        this.facilityCapacity = facilityCapacity;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityLocation() {
        return facilityLocation;
    }

    public void setFacilityLocation(String facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public String getFacilityPicture() {
        return facilityPicture;
    }

    public void setFacilityPicture(String facilityPicture) {
        this.facilityPicture = facilityPicture;
    }

    public String getFacilityStatus() {
        return facilityStatus;
    }

    public void setFacilityStatus(String facilityStatus) {
        this.facilityStatus = facilityStatus;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public int getFacilityCapacity() {
        return facilityCapacity;
    }

    public void setFacilityCapacity(int facilityCapacity) {
        this.facilityCapacity = facilityCapacity;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String toString() {
        return "Facility{" +
                "facilityID=" + facilityID +
                ", facilityName='" + facilityName + '\'' +
                ", facilityLocation='" + facilityLocation + '\'' +
                ", facilityType='" + facilityType + '\'' +
                ", facilityStatus='" + facilityStatus + '\'' +
                ", facilityCapacity='" + facilityCapacity + '\'' +
                ", facilityPicture='" + facilityPicture + '\'' +
                '}';
    }
}
