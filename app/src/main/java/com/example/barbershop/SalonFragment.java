package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalonFragment extends Fragment implements SalonAdapter.OnNoteListener {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/getShops.php";
    private RecyclerView recyclerView;
    private SalonAdapter adapter;
    private List<SalonItem> salonItemList;

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

        loadSalons();
    }

    private void loadSalons(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray services = new JSONArray(response);
                    for (int i = 0 ; i < services.length() ; i++){
                        JSONObject serviceObject = services.getJSONObject(i);
                        String salonName = serviceObject.getString("ShopName");
                        String address = serviceObject.getString("Address");
                        int id = serviceObject.getInt("BarbershopID");
                        SalonItem salonItem = new SalonItem(R.drawable.herry_poer,salonName,address,id); //TODO CHANGE THE IMAGE FUNCTIONALITY
                        salonItemList.add(salonItem);
                        Log.d("php", "salon name" + salonName);
                    }

                    adapter = new SalonAdapter(getActivity(), salonItemList, SalonFragment.this::onNoteClick);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    @Override
    public void onNoteClick(View v, int position) {
        salonItemList.get(position);
        Intent intent = new Intent(getActivity() , ShopPageActivity.class);
        intent.putExtra("barbershopID",salonItemList.get(position).getId());
        intent.putExtra("shopName",salonItemList.get(position).getSalonName());
        startActivity(intent);
    }
}
