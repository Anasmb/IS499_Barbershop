package com.example.barbershop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SalonFragment extends Fragment {

    RecyclerView recyclerView;
    SalonAdapter adapter;
    List<SalonItem> salonItemList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_salon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        salonItemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.salons_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        salonItemList.add(new SalonItem(R.drawable.herry_poer, "HERRY POER" , "Prince Saud al faiasl, AL Rawdah, Riyadh. 23424, Saudi Arabia" , "4.5"));

        adapter = new SalonAdapter(getContext(), salonItemList);
        recyclerView.setAdapter(adapter);
    }
}
