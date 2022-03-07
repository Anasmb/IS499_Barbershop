package com.example.barbershop;

public class SalonItem {

    private int salonImage;
    private String salonName;
    private String address;

    public SalonItem(int salonImage, String salonName, String address) {
        this.salonImage = salonImage;
        this.salonName = salonName;
        this.address = address;
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

}
