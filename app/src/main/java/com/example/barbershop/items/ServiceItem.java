package com.example.barbershop.items;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceItem implements Parcelable {

    private String serviceName;
    private String servicePrice;

    public ServiceItem(String serviceName, String servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    protected ServiceItem(Parcel in) {
        serviceName = in.readString();
        servicePrice = in.readString();
    }

    public static final Creator<ServiceItem> CREATOR = new Creator<ServiceItem>() {
        @Override
        public ServiceItem createFromParcel(Parcel in) {
            return new ServiceItem(in);
        }

        @Override
        public ServiceItem[] newArray(int size) {
            return new ServiceItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serviceName);
        parcel.writeString(servicePrice);
    }
}
