package com.example.facility_bookuitm.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {


    private int reserveID;

    private String reserveDate;


    private String reserveTime;


    private String reservePurpose;


    private String reserveStatus;


    private int user_id;


    private int facility_id;

//    @SerializedName("user")
//    private User user;

//    @SerializedName("facility")
//    private Facility facility;

    // Getters and Setters
    public int getReserveID() { return reserveID; }
    public void setReserveID(int reserveID) { this.reserveID = reserveID; }

    public String getReserveDate() { return reserveDate; }
    public void setReserveDate(String reserveDate) { this.reserveDate = reserveDate; }

    public String getReserveTime() { return reserveTime; }
    public void setReserveTime(String reserveTime) { this.reserveTime = reserveTime; }

    public String getReservePurpose() { return reservePurpose; }
    public void setReservePurpose(String reservePurpose) { this.reservePurpose = reservePurpose; }

    public String getReserveStatus() { return reserveStatus; }
    public void setReserveStatus(String reserveStatus) { this.reserveStatus = reserveStatus; }

//    public Facility getFacility() { return facility; }
//
//    public void setFacility(Facility facility) { this.facility = facility; }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(int facility_id) {
        this.facility_id = facility_id;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}


