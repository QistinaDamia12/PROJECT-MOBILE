package com.example.facility_bookuitm;

public class Facility {
    private String name;
    private String location;
    private int imageResId;
    private String capacity; // Pastikan ada ini

    // Update Constructor untuk terima 4 parameter
    public Facility(String name, String location, int imageResId, String capacity) {
        this.name = name;
        this.location = location;
        this.imageResId = imageResId;
        this.capacity = capacity;
    }

    // Getters
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getImageResId() { return imageResId; }
    public String getCapacity() { return capacity; } // Ini yang dipanggil oleh Adapter
}
