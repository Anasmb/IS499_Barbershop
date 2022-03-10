package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/getService.php";
    private ImageView backBtn;
    private MaterialButton continueBtn;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<ServiceItem> serviceItemList;
    private int shopID;
    private String barberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        barberName = getIntent().getExtras().getString("barberName");
        shopID = getIntent().getExtras().getInt("barbershopID");
        SQL_URL += "?BarbershopID=" + shopID;

        serviceItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.services_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        backBtn = findViewById(R.id.selectService_backButton);
        backBtn.setOnClickListener(backBtnListener);
        continueBtn = findViewById(R.id.selectService_continueButton);
        continueBtn.setOnClickListener(continueBtnListener);

        loadServices();
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener continueBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!adapter.listOfSelectedServices().isEmpty()) { // must select at least one service to go next activity
                Intent intent = new Intent(getApplicationContext(), SelectDateActivity.class);
                intent.putExtra("barbershopID", shopID);
                intent.putExtra("barberName", barberName);
                intent.putParcelableArrayListExtra("services", adapter.listOfSelectedServices());
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Please choose a service" , Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadServices(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray services = new JSONArray(response);
                    for (int i = 0 ; i < services.length() ; i++){
                        JSONObject serviceObject = services.getJSONObject(i);
                        String name = serviceObject.getString("name");
                        double price = serviceObject.getDouble("price");
                        ServiceItem serviceItem = new ServiceItem(name,String.valueOf(price));
                        serviceItemList.add(serviceItem);
                    }

                    adapter = new ServiceAdapter(getApplicationContext(), serviceItemList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }


}