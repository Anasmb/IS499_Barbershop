package com.example.barbershop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.R;
import com.example.barbershop.items.FeedbackItem;
import com.example.barbershop.items.OfferItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>{

    private Context mContext;
    private List<OfferItem> offerItemList;

    public OfferAdapter(Context mContext, List<OfferItem> offerItemList) {
        this.mContext = mContext;
        this.offerItemList = offerItemList;
    }

    @NonNull
    @NotNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.offer_item, null);
        OfferAdapter.OfferViewHolder holder = new OfferAdapter.OfferViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OfferAdapter.OfferViewHolder holder, int position) {
        OfferItem offerItem = offerItemList.get(position);

        holder.header.setText(String.valueOf(offerItem.getOfferHeader()));
        holder.description.setText(offerItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return offerItemList.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView description;

        public OfferViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.offerHeaderText);
            description = itemView.findViewById(R.id.offerDescription);
        }

    }


}
