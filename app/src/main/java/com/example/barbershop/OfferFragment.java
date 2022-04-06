package com.example.barbershop;

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
import com.example.barbershop.adapters.OfferAdapter;
import com.example.barbershop.adapters.SalonAdapter;
import com.example.barbershop.items.OfferItem;
import com.example.barbershop.items.SalonItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfferFragment extends Fragment {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/getOffers.php";
    private RecyclerView recyclerView;
    private OfferAdapter adapter;
    private List<OfferItem> offerItemList;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        offerItemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.offerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadOffers();
    }

    private void loadOffers(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray offers = new JSONArray(response);
                    for (int i = 0 ; i < offers.length() ; i++){
                        JSONObject offerObject = offers.getJSONObject(i);
                        String header = offerObject.getString("header");
                        String description = offerObject.getString("description");
                        double discount = offerObject.getDouble("discount");
                        double targetPrice = offerObject.getDouble("target");
                        OfferItem offerItem = new OfferItem(header,description,discount,targetPrice);
                        offerItemList.add(offerItem);
                    }

                    adapter = new OfferAdapter(getActivity(), offerItemList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Log.d("php", "onErrorResponse: " + error);
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
