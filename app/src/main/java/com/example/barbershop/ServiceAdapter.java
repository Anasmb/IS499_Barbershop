package com.example.barbershop;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context mContext;
    private List<ServiceItem> serviceItemList;
    ArrayList<ServiceItem> selectedServices;

    public ServiceAdapter(Context mContext, List<ServiceItem> serviceItemList){
        this.mContext = mContext;
        this.serviceItemList = serviceItemList;
    }


    @NonNull
    @NotNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.service_item, null);
        ServiceViewHolder holder = new ServiceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceAdapter.ServiceViewHolder holder, int position) {
        ServiceItem serviceItem = serviceItemList.get(position);
        holder.price.setText(serviceItem.getServicePrice() + " SR");
        holder.selectCheckBox.setText(serviceItem.getServiceName());
        holder.selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //add selected services to array
                if(holder.selectCheckBox.isChecked()){
                    selectedServices.add(serviceItem);
                }
                else{ // remove the services from array when de selected
                    selectedServices.remove(serviceItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

    public ArrayList<ServiceItem> listOfSelectedServices(){
        return selectedServices;
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        CheckBox selectCheckBox;
        TextView price;


        public ServiceViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            selectCheckBox = itemView.findViewById(R.id.serviceCheckBox);
            price = itemView.findViewById(R.id.servicePriceTextView);
            selectedServices = new ArrayList<>();
        }



    }



}
