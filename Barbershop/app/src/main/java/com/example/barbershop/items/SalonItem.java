package com.example.barbershop.items;

public class SalonItem {

    private String salonImage;
    private String salonName;
    private String address;
    private String phoneNumber;
    private int id;

    public SalonItem(String salonImage, String salonName, String address ,String phoneNumber ,int id) {
        this.salonImage = salonImage;
        this.salonName = salonName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getSalonImage() {
        return salonImage;
    }

    public String getSalonName() {
        return salonName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId(){
        return id;
    }

}
