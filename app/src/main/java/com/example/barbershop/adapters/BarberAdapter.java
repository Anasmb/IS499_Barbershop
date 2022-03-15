package com.example.barbershop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.items.BarberItem;
import com.example.barbershop.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberViewHolder> {

    private Context mContext;
    private List<BarberItem> barberItemList;
    private OnNoteListener mOnNoteListener;

    public BarberAdapter(Context mContext, List<BarberItem> barberItemList ,OnNoteListener onNoteListener){
        this.mContext = mContext;
        this.barberItemList = barberItemList;
        this.mOnNoteListener = onNoteListener;
    }


    @NonNull
    @NotNull
    @Override
    public BarberViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.barber_item, null);
        BarberViewHolder holder = new BarberViewHolder(view,mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BarberAdapter.BarberViewHolder holder, int position) {
        BarberItem barberItem = barberItemList.get(position);

        holder.barberName.setText(barberItem.getBarberName());
        holder.experience.setText(barberItem.getExperience());
        holder.nationality.setText(barberItem.getNationality());
    }

    @Override
    public int getItemCount() {
        return barberItemList.size();
    }

    class BarberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView barberName;
        TextView experience;
        TextView nationality;
       OnNoteListener onNoteListener;

        public BarberViewHolder(@NonNull @NotNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            barberName = itemView.findViewById(R.id.barberNameTextView);
            experience = itemView.findViewById(R.id.barberExperienceTextView);
            nationality = itemView.findViewById(R.id.barberNationalityTextView);
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
