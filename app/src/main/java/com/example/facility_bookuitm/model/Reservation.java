package com.example.facility_bookuitm.model;

public class Reservation
{
    private int reserveID;
    private String reserveDate;
    private String reserveTime;
    private String reservePurpose;
    private String userID;
    private int facilityID;

    public int getReserveID() {
        return reserveID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getReservePurpose() {
        return reservePurpose;
    }

    public void setReservePurpose(String reservePurpose) {
        this.reservePurpose = reservePurpose;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }
}
