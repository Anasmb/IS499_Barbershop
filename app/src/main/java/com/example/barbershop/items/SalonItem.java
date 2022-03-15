package com.example.barbershop.items;

public class SalonItem {

    private int salonImage;
    private String salonName;
    private String address;
    private int id;

    public SalonItem(int salonImage, String salonName, String address , int id) {
        this.salonImage = salonImage;
        this.salonName = salonName;
        this.address = address;
        this.id = id;
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

    public int getId(){
        return id;
    }

}
