package com.example.facility_bookuitm.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("reserveID")
    private int reserveID;

    @SerializedName("reserveDate")
    private String reserveDate;

    @SerializedName("reserveTime")
    private String reserveTime;

    @SerializedName("reservePurpose")
    private String reservePurpose;

    @SerializedName("reserveStatus")
    private String reserveStatus;

    @SerializedName("id")
    private int id;

    @SerializedName("facilityID")
    private int facilityID;

    @SerializedName("facility")
    private Facility facility;

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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFacilityID() { return facilityID; }
    public void setFacilityID(int facilityID) { this.facilityID = facilityID; }

    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }
}


