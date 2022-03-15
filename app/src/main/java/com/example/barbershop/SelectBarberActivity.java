package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershop.adapters.BarberAdapter;
import com.example.barbershop.items.BarberItem;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectBarberActivity extends AppCompatActivity implements BarberAdapter.OnNoteListener {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/getBarbers.php";
    private ImageView backBtn;
    private MaterialButton continueBtn;
    private RecyclerView recyclerView;
    private BarberAdapter adapter;
    private List<BarberItem> barberItemList;
    private int shopID;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_barber);

        shopID = getIntent().getExtras().getInt("barbershopID");
        shopName = getIntent().getExtras().getString("shopName");
        SQL_URL += "?BarbershopID=" + shopID;

        barberItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.barbers_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        backBtn = findViewById(R.id.selectBarber_backButton);
        backBtn.setOnClickListener(backBtnListener);

        loadBarbers();
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };


    private void loadBarbers(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray Barbers = new JSONArray(response);
                    for (int i = 0 ; i < Barbers.length() ; i++){
                        JSONObject barbersObject = Barbers.getJSONObject(i);
                        String name = barbersObject.getString("name");
                        String experience = barbersObject.getString("experience");
                        String nationality = barbersObject.getString("nationality");
                        BarberItem barberItem = new BarberItem(name,experience,nationality);
                        barberItemList.add(barberItem);
                    }
                    adapter = new BarberAdapter(getApplicationContext(), barberItemList,SelectBarberActivity.this::onNoteClick);
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

    @Override
    public void onNoteClick(View v, int position) {
        Intent intent = new Intent(getApplicationContext(),SelectServiceActivity.class);
        intent.putExtra("barberName",barberItemList.get(position).getBarberName());
        intent.putExtra("barbershopID",shopID);
        intent.putExtra("shopName",shopName);
        startActivity(intent);
    }
}