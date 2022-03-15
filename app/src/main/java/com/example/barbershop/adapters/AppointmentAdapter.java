package com.example.barbershop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.R;
import com.example.barbershop.items.AppointmentItem;
import com.example.barbershop.items.SalonItem;

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
    }

    @Override
    public int getItemCount() {
        return appointmentItemList.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView barbershopName, barberName, dateTime, price, serviceAt ,status;
        OnItemListener onItemListener;

        public AppointmentViewHolder(@NonNull @NotNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            barbershopName = itemView.findViewById(R.id.appointmentBarbershopName);
            barberName = itemView.findViewById(R.id.appointmentBarberName);
            dateTime = itemView.findViewById(R.id.appointmentDateTimeText);
            price = itemView.findViewById(R.id.appointmentPriceTxt);
            status = itemView.findViewById(R.id.appointmentStatusText);
            serviceAt = itemView.findViewById(R.id.appointmentServiceLocation);

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

}
