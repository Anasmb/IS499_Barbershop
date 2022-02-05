package com.example.barbershop;

public class SalonItem {

    private int salonImage;
    private String salonName;
    private String address;
    private String distance;
    private String rating;

    public SalonItem(int salonImage, String salonName, String address, String distance, String rating) {
        this.salonImage = salonImage;
        this.salonName = salonName;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
    }

    public int getSalonImage() {
        return salonImage;
    }

    public String getSalonName() {
        return salonName;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

    public String getRating() {
        return rating;
    }
}
