package com.example.barbershop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.R;
import com.example.barbershop.RateActivity;
import com.example.barbershop.items.AppointmentItem;
import com.example.barbershop.items.SalonItem;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context mContext;
    private List<AppointmentItem> appointmentItemList;
    private OnItemListener mOnItemListener;

    public AppointmentAdapter(Context mContext, List<AppointmentItem> appointmentItems , OnItemListener onItemListener) {
        this.mContext = mContext;
        this.appointmentItemList = appointmentItems;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @NotNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.appointment_item, null);
        AppointmentAdapter.AppointmentViewHolder holder = new AppointmentAdapter.AppointmentViewHolder(view,mOnItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        AppointmentItem appointmentItem = appointmentItemList.get(position);

        holder.barbershopName.setText(appointmentItem.getBarbershop());
        holder.barberName.setText(appointmentItem.getBarber());
        holder.dateTime.setText(appointmentItem.getDateTime());
        holder.price.setText(appointmentItem.getPrice());
        holder.serviceAt.setText(appointmentItem.getServiceAt());
        holder.status.setText(appointmentItem.getStatus());

        if (holder.status.getText().toString().equals("Finished")){
            holder.status.setTextColor(Color.parseColor("#AAAAAA"));
            holder.cancelText.setVisibility(View.GONE);
        }
        else if (holder.status.getText().toString().equals("Confirmed")){
            holder.status.setTextColor(Color.parseColor("#00A521"));
            holder.cancelText.setVisibility(View.GONE);
        }
        else if (holder.status.getText().toString().equals("Declined")){
            holder.status.setTextColor(Color.parseColor("#A50000"));
            holder.cancelText.setVisibility(View.GONE);
        }

        holder.cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cancelText.setVisibility(View.GONE);
                cancelAppointment(appointmentItem.getAppointmentID());
                Toast.makeText(mContext.getApplicationContext(), "Appointment Canceled" , Toast.LENGTH_LONG).show();
                AppointmentAdapter.this.notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentItemList.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView barbershopName, barberName, dateTime, price, serviceAt ,status, cancelText ;
        OnItemListener onItemListener;

        public AppointmentViewHolder(@NonNull @NotNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            barbershopName = itemView.findViewById(R.id.appointmentBarbershopName);
            barberName = itemView.findViewById(R.id.appointmentBarberName);
            dateTime = itemView.findViewById(R.id.appointmentDateTimeText);
            price = itemView.findViewById(R.id.appointmentPriceTxt);
            status = itemView.findViewById(R.id.appointmentStatusText);
            serviceAt = itemView.findViewById(R.id.appointmentServiceLocation);
            cancelText = itemView.findViewById(R.id.appointmentCancelText);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(view,getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(View view, int position);
    }

    private void cancelAppointment(int appointmentID){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "appointmentID";
                String[] data = new String[1];
                data[0] = String.valueOf(appointmentID);
                PutData putData = new PutData("http://188.54.243.108/barbershop-php/appointment/deleteAppointment.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.d("php", result);
                    }
                }

            }
        });
    }

}
