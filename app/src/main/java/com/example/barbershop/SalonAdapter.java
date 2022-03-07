package com.example.barbershop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.SalonViewHolder> {

    private Context mContext;
    private List<SalonItem> salonItemList;
    private OnNoteListener mOnNoteListener;

    public SalonAdapter(Context mContext, List<SalonItem> salonItemList, OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.salonItemList = salonItemList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @NotNull
    @Override
    public SalonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.salon_item, null);
        SalonViewHolder holder = new SalonViewHolder(view,mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SalonAdapter.SalonViewHolder holder, int position) {
        SalonItem salonItem = salonItemList.get(position);

        holder.salonImage.setImageDrawable(mContext.getResources().getDrawable(salonItem.getSalonImage()));
        holder.salonName.setText(salonItem.getSalonName());
        holder.address.setText(salonItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return salonItemList.size();
    }



    class SalonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView salonImage;
        TextView salonName;
        TextView address;
        OnNoteListener onNoteListener;

        public SalonViewHolder(@NonNull @NotNull View itemView,OnNoteListener onNoteListener) {
            super(itemView);

            salonImage = itemView.findViewById(R.id.salonLogoImageView);
            salonName = itemView.findViewById(R.id.salonNameTextView);
            address = itemView.findViewById(R.id.addressTextView);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
