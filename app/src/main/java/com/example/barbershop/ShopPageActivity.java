package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopPageActivity extends AppCompatActivity {

    private String SQL_URL = "http://188.54.243.108/barbershop-php/hoursAPI.php";
    private TextView shopName;
    private int shopID;
    private String address, phoneNumber;
    private ImageView backBtn;
    private MaterialButton bookBtn;
    private LinearLayout callLayout, mapLayout, galleryLayout,feedbackLayout;
    private TextView sundayTime, mondayTime, tuesdayTime, wednesdayTime, thursdayTime, fridayTime, saturdayTime;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        viewsInitialization();

        shopID = getIntent().getExtras().getInt("barbershopID");
        shopName.setText(getIntent().getExtras().getString("shopName"));
        address = getIntent().getExtras().getString("address");
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        SQL_URL += "?BarbershopID=" + shopID;

        loadHours();
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener bookBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!preferences.getString("customerID","").isEmpty()){ // check if he is logged in or no
                Intent intent = new Intent(getApplicationContext() , SelectBarberActivity.class);
                intent.putExtra("barbershopID",shopID);
                intent.putExtra("shopName",shopName.getText());
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Please login first" , Toast.LENGTH_LONG).show();
            }

        }
    };

    private View.OnClickListener callLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    };

    private View.OnClickListener mapLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String coordinates[] = address.split("/"); // get the latitude and longitude
            String uri = "http://maps.google.com/maps?q=loc:" + coordinates[1] + "," + coordinates[2];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    };

    private View.OnClickListener galleryLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),GalleryActivity.class);
            intent.putExtra("barbershopID",shopID);
            startActivity(intent);
        }
    };

    private View.OnClickListener feedbackLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),FeedbackActivity.class);
            intent.putExtra("barbershopID",shopID);
            startActivity(intent);
        }
    };

    private void loadHours(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                String openingHours[] = result.split(",");
                sundayTime.setText(openingHours[0] + " - " + openingHours[1]);
                mondayTime.setText(openingHours[2] + " - " + openingHours[3]);
                tuesdayTime.setText(openingHours[4] + " - " + openingHours[5]);
                wednesdayTime.setText(openingHours[6] + " - " + openingHours[7]);
                thursdayTime.setText(openingHours[8] + " - " + openingHours[9]);
                fridayTime.setText(openingHours[10] + " - " + openingHours[11]);
                saturdayTime.setText(openingHours[12] + " - " + openingHours[13]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void viewsInitialization(){
        backBtn = findViewById(R.id.shopPage_backButton);
        backBtn.setOnClickListener(backBtnListener);
        bookBtn = findViewById(R.id.bookButton);
        bookBtn.setOnClickListener(bookBtnListener);
        shopName = findViewById(R.id.shopPage_salonNameText);
        callLayout = findViewById(R.id.callBarbershopLayout);
        callLayout.setOnClickListener(callLayoutListener);
        mapLayout = findViewById(R.id.barbershopLocationLayout);
        mapLayout.setOnClickListener(mapLayoutListener);
        galleryLayout = findViewById(R.id.shopPageGalleryLayout);
        galleryLayout.setOnClickListener(galleryLayoutListener);
        feedbackLayout = findViewById(R.id.shopPageFeedbackLayout);
        feedbackLayout.setOnClickListener(feedbackLayoutListener);

        sundayTime = findViewById(R.id.sundayTimeText);
        mondayTime = findViewById(R.id.mondayTimeText);
        tuesdayTime = findViewById(R.id.tuesdayTimeText);
        wednesdayTime = findViewById(R.id.wednesdayTimeText);
        thursdayTime = findViewById(R.id.thursdayTimeText);
        fridayTime = findViewById(R.id.fridayTimeText);
        saturdayTime = findViewById(R.id.saturdayTimeText);
    }

}