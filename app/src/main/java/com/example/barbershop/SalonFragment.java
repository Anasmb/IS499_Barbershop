package com.example.barbershop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.barbershop.adapters.SalonAdapter;
import com.example.barbershop.items.SalonItem;
import com.google.android.material.textfield.TextInputEditText;

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
    private SharedPreferences preferences;
    private TextView welcomeText;
    private TextInputEditText searchSalonEditText;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_salon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);


        welcomeText = view.findViewById(R.id.welcomeGuestText);
        searchSalonEditText = view.findViewById(R.id.searchSalonEditText);
        searchSalonEditText.addTextChangedListener(searchListener);

        if (!preferences.getString("name","").isEmpty()){
            welcomeText.setText("Welcome " + preferences.getString("name",""));
        }

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
                    JSONArray salons = new JSONArray(response);
                    for (int i = 0 ; i < salons.length() ; i++){
                        JSONObject salonObject = salons.getJSONObject(i);
                        String image = salonObject.getString("Image");
                        String salonName = salonObject.getString("ShopName");
                        String address = salonObject.getString("Address");
                        String phoneNumber = salonObject.getString("PhoneNumber");
                        int id = salonObject.getInt("BarbershopID");
                        SalonItem salonItem = new SalonItem(image,salonName,address,phoneNumber,id);
                        salonItemList.add(salonItem);
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
        intent.putExtra("address",salonItemList.get(position).getAddress());
        intent.putExtra("phoneNumber",salonItemList.get(position).getPhoneNumber());
        startActivity(intent);
    }

    private TextWatcher searchListener = new TextWatcher() { // search for barbershop
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
           ArrayList<SalonItem> filteredList = new ArrayList<>();
           for (SalonItem item : salonItemList){
               if (item.getSalonName().toLowerCase().contains(editable.toString().toLowerCase())){
                    filteredList.add(item);
               }
           }
           adapter.filterList(filteredList);

        }
    };


}
