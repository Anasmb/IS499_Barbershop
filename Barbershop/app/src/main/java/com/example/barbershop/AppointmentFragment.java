package com.example.barbershop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershop.adapters.AppointmentAdapter;
import com.example.barbershop.adapters.SalonAdapter;
import com.example.barbershop.items.AppointmentItem;
import com.example.barbershop.items.SalonItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentFragment extends Fragment implements AppointmentAdapter.OnItemListener {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/appointment/getAppointment.php";
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<AppointmentItem> appointmentItemList;
    private SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        if (preferences.getString("name","").isEmpty()){ // check if customer is not logged in, then go to signin page
            Intent intent = new Intent(getActivity(),SigninActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        SQL_URL += "?customerID=" + preferences.getString("customerID","");

        swipeRefreshLayout = view.findViewById(R.id.appointmentRefresh);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        progressBar = view.findViewById(R.id.appointmentProgress);

        appointmentItemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.appointments_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadAppointments();
    }

    private void loadAppointments(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("php", "loadAppointments Response: " + response);
                try {
                    JSONArray appointments = new JSONArray(response);
                    for (int i = 0 ; i < appointments.length() ; i++){
                        JSONObject appointmentObject = appointments.getJSONObject(i);
                        int appointmentID = appointmentObject.getInt("appointmentID");
                        int barbershopID = appointmentObject.getInt("barbershopID");
                        String barbershopName = appointmentObject.getString("barbershopName");
                        String barberName = appointmentObject.getString("barberName");
                        String serviceAt = appointmentObject.getString("serviceLocation");
                        String dateTime = appointmentObject.getString("date") + " , " + appointmentObject.getString("time");
                        String totalPrice = appointmentObject.getString("totalPrice");
                        String status = appointmentObject.getString("status");
                        String barbershopAddress = appointmentObject.getString("barbershopAddress");
                        AppointmentItem appointmentItem = new AppointmentItem(appointmentID,barbershopID,barbershopName,barberName,dateTime,totalPrice,barbershopAddress,serviceAt,status);
                        appointmentItemList.add(appointmentItem);
                    }
                    adapter = new AppointmentAdapter(getActivity(), appointmentItemList, AppointmentFragment.this::onItemClick);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Log.d("php", "Unable to connect to Database");
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onItemClick(View view, int position) { //start google map with a pin on barbershop location
        String coordinates[] = appointmentItemList.get(position).getAddress().split("/"); // get the latitude and longitude
        String uri = "http://maps.google.com/maps?q=loc:" + coordinates[1] + "," + coordinates[2];
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            appointmentItemList.clear();
            adapter = new AppointmentAdapter(getActivity(), appointmentItemList, AppointmentFragment.this::onItemClick);
            recyclerView.setAdapter(adapter);
            loadAppointments();
            swipeRefreshLayout.setRefreshing(false);
        }
    };
}
