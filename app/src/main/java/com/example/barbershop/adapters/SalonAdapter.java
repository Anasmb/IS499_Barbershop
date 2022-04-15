package com.example.barbershop.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.R;
import com.example.barbershop.items.SalonItem;

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

        String stringAddress[] = salonItem.getAddress().split("/");

        holder.salonName.setText(salonItem.getSalonName());
        holder.address.setText(stringAddress[0]);

        byte[] imageAsBytes = Base64.decode(salonItem.getSalonImage().getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        holder.salonImage.setImageBitmap(bitmap);

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
            onNoteListener.onNoteClick(view,getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(View v, int position);
    }
}
