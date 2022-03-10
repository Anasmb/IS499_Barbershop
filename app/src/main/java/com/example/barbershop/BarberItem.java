package com.example.barbershop;

public class BarberItem {

    private String barberName;
    private String experience;
    private String nationality;

    public BarberItem(String barberName, String experience, String nationality) {
        this.barberName = barberName;
        this.experience = experience;
        this.nationality = nationality;
    }

    public String getBarberName() {
        return barberName;
    }

    public String getExperience() {
        return experience;
    }

    public String getNationality() {
        return nationality;
    }
}
