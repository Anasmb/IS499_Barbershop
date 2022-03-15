package com.example.barbershop.items;

public class AppointmentItem {

    private String barbershop;
    private String barber;
    private String dateTime;
    private String price;
    private String address;
    private String serviceAt;
    private String status;

    public AppointmentItem(String barbershop, String barber, String dateTime, String price, String address, String serviceAt ,String status) {
        this.barbershop = barbershop;
        this.barber = barber;
        this.dateTime = dateTime;
        this.price = price;
        this.address = address;
        this.serviceAt = serviceAt;
        this.status = status;
    }

    public String getBarbershop() {
        return barbershop;
    }

    public String getBarber() {
        return barber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getServiceAt() {
        return serviceAt;
    }
}
