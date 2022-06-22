package com.example.barbershop.items;

public class OfferItem {

    private String offerHeader;
    private String description;
    private double discount, targetPrice;

    public OfferItem(String offerHeader, String description, double discount, double targetPrice) {
        this.offerHeader = offerHeader;
        this.description = description;
        this.discount = discount;
        this.targetPrice = targetPrice;
    }

    public String getOfferHeader() {
        return offerHeader;
    }

    public String getDescription() {
        return description;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTargetPrice() {
        return targetPrice;
    }
}
