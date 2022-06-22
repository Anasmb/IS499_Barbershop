package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershop.adapters.OfferAdapter;
import com.example.barbershop.items.OfferItem;
import com.example.barbershop.items.ServiceItem;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class SummaryActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/getOffers.php";
    private SharedPreferences preferences;
    private LinearLayout creditCardLayout, payAtStoreLayout, servicesLayout;
    private MaterialButton bookButton;
    private ImageView backBtn;
    private int shopID;
    private String barberName, date, time, address, shopName , services;
    private ArrayList<ServiceItem> selectedServices;
    private TextView barbershopNameText, barberNameText, dateText, timeText, grandTotalPrice , discountAmount;
    private double totalPrice , tempDiscount , targetPriceOld = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        selectedServices = getIntent().getExtras().getParcelableArrayList("services");
        shopID = getIntent().getExtras().getInt("barbershopID");
        barberName = getIntent().getExtras().getString("barberName");
        address = getIntent().getExtras().getString("address");
        shopName = getIntent().getExtras().getString("shopName");
        date = getIntent().getExtras().getString("date");
        time = getIntent().getExtras().getString("time");

        viewsInitialization();
        loadSummary();
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener creditCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setBackgroundColor(getResources().getColor(R.color.quantum_grey300));
            view.setTag("selected");
            payAtStoreLayout.setBackgroundColor(getResources().getColor(R.color.white));
            payAtStoreLayout.setTag("not selected");
        }
    };

    private View.OnClickListener payAtStoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setBackgroundColor(getResources().getColor(R.color.quantum_grey300));
            view.setTag("selected");
            creditCardLayout.setBackgroundColor(getResources().getColor(R.color.white));
            creditCardLayout.setTag("not selected");
        }
    };

    private View.OnClickListener bookListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (payAtStoreLayout.getTag().equals("not selected") && creditCardLayout.getTag().equals("not selected")) {
                Toast.makeText(getApplicationContext(), "Select payment method", Toast.LENGTH_SHORT).show();
            }
            else if(payAtStoreLayout.getTag().equals("selected")) {
                saveAppointmentToDB();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Credit card are not available right now", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadSummary() {

        barberNameText.setText(barberName);
        dateText.setText(date);
        timeText.setText(", " + time);
        barbershopNameText.setText(shopName);

        Typeface typeface = getResources().getFont(R.font.roboto_regular);
        services = "";

        for (int i = 0; i < selectedServices.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(32, 16, 32, 16);
            servicesLayout.addView(linearLayout);

            TextView serviceName = new TextView(this);
            serviceName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            serviceName.setText(selectedServices.get(i).getServiceName());
            serviceName.setTextColor(getResources().getColor(R.color.black));
            serviceName.setTextSize(16);
            serviceName.setTypeface(typeface);
            linearLayout.addView(serviceName);

            TextView servicePrice = new TextView(this);
            servicePrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            servicePrice.setText(selectedServices.get(i).getServicePrice());
            servicePrice.setTextColor(Color.parseColor("#008E2A"));
            servicePrice.setTextSize(16);
            servicePrice.setTypeface(typeface);
            servicePrice.setGravity(Gravity.RIGHT);
            linearLayout.addView(servicePrice);

            services += selectedServices.get(i).getServiceName() + " , ";
            Log.d("debug", "services = " + services);
            totalPrice += Double.parseDouble(selectedServices.get(i).getServicePrice());
        }

        applyDiscount();

    }

    private void saveAppointmentToDB() {

        bookButton.setClickable(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[12];
                field[0] = "totalPrice";
                field[1] = "discount";
                field[2] = "time";
                field[3] = "date";
                field[4] = "services";
                field[5] = "status";
                field[6] = "customerAddress";
                field[7] = "serviceLocation";
                field[8] = "barbershopName";
                field[9] = "barberName";
                field[10] = "barbershopID";
                field[11] = "customerID";
                //Creating array for data
                String[] data = new String[12];
                data[0] = String.valueOf(totalPrice);
                data[1] = String.valueOf(discountAmount.getText().toString());
                data[2] = time;
                data[3] = date;
                data[4] = services;
                data[5] = "Pending";
                data[6] = address == null ? "" : address;
                data[7] =  address == null ? "At Barbershop" : "At House";
                data[8] = shopName;
                data[9] = barberName;
                data[10] = String.valueOf(shopID);
                data[11] = preferences.getString("customerID","");
                PutData putData = new PutData("http://192.168.100.6/barbershop-php/appointment/addAppointment.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Add Success")) {
                            Log.d("php", result);
                            Toast.makeText(getApplicationContext(), "Please wait for confirmation", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SummaryActivity.this,MainActivity.class));
                            finish();
                        } else { // All fields are required
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            Log.d("php", result);
                            bookButton.setClickable(true);
                        }
                    }
                }
            }
        });

    }

    private void applyDiscount(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray offers = new JSONArray(response);
                    for (int i = 0 ; i < offers.length() ; i++){
                        JSONObject offerObject = offers.getJSONObject(i);
                        double discount = offerObject.getDouble("discount");
                        double targetPriceNew= offerObject.getDouble("target");
                        if (totalPrice >= targetPriceNew && targetPriceNew > targetPriceOld){
                            tempDiscount = discount;
                            targetPriceOld = targetPriceNew;
                        }
                    }
                    discountAmount.setText(String.format("%.2f" , totalPrice * tempDiscount));
                    totalPrice = totalPrice - (totalPrice * tempDiscount);
                    grandTotalPrice.setText(String.format("%.2f" , totalPrice));
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

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void viewsInitialization(){
        creditCardLayout = findViewById(R.id.creditCardLayout);
        creditCardLayout.setOnClickListener(creditCardListener);
        creditCardLayout.setTag("not selected");
        payAtStoreLayout = findViewById(R.id.payAtStoreLayout);
        payAtStoreLayout.setOnClickListener(payAtStoreListener);
        payAtStoreLayout.setTag("not selected");
        servicesLayout = findViewById(R.id.summaryServicesLayout);
        bookButton = findViewById(R.id.summaryBookButton);
        bookButton.setOnClickListener(bookListener);
        backBtn = findViewById(R.id.summary_backButton);
        backBtn.setOnClickListener(backBtnListener);

        barbershopNameText = findViewById(R.id.summaryBarbershopName);
        barberNameText = findViewById(R.id.summaryBarberName);
        dateText = findViewById(R.id.summaryDate);
        timeText = findViewById(R.id.summaryTime);
        grandTotalPrice = findViewById(R.id.grandTotalPriceText);
        discountAmount = findViewById(R.id.discountAmountText);
    }

}