package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershop.items.ServiceItem;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private LinearLayout creditCardLayout, payAtStoreLayout, servicesLayout;
    private MaterialButton bookButton;
    private ImageView backBtn;
    private int shopID;
    private String barberName, date, time, address, shopName , services;
    private ArrayList<ServiceItem> selectedServices;
    private TextView barbershopNameText, barberNameText, dateText, timeText, grandTotalPrice;
    private double totalPrice = 0.0;

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
            else {
                saveAppointmentToDB();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
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
        grandTotalPrice.setText(String.valueOf(totalPrice));
    }

    private void saveAppointmentToDB() {


        bookButton.setClickable(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[11];
                field[0] = "totalPrice";
                field[1] = "time";
                field[2] = "date";
                field[3] = "services";
                field[4] = "status";
                field[5] = "customerAddress";
                field[6] = "serviceLocation";
                field[7] = "barbershopName";
                field[8] = "barberName";
                field[9] = "barbershopID";
                field[10] = "customerID";
                //Creating array for data
                String[] data = new String[11];
                data[0] = String.valueOf(totalPrice);
                data[1] = time;
                data[2] = date;
                data[3] = services;
                data[4] = "Pending";
                data[5] = address == null ? "" : address;
                data[6] =  address == null ? "At Barbershop" : "At House";
                data[7] = shopName;
                data[8] = barberName;
                data[9] = String.valueOf(shopID);
                data[10] = preferences.getString("customerID","");
                PutData putData = new PutData("http://192.168.100.6/barbershop-php/addAppointment.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Add Success")) {
                            Log.d("php", result);
                            Toast.makeText(getApplicationContext(), "Please wait for confirmation", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { // All fields are required
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            Log.d("php", result);
                            bookButton.setClickable(true);
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });

    }

}