package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopPageActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/hoursAPI.php";
    private TextView shopName;
    private int shopID;
    private ImageView backBtn;
    private TextView sundayTime, mondayTime, tuesdayTime, wednesdayTime, thursdayTime, fridayTime, saturdayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        backBtn = findViewById(R.id.shopPage_backButton);
        backBtn.setOnClickListener(backBtnListener);
        shopName = findViewById(R.id.shopPage_salonNameText);

        shopID = getIntent().getExtras().getInt("barbershopID");
        shopName.setText(getIntent().getExtras().getString("shopName"));
        SQL_URL += "?BarbershopID=" + shopID;

        sundayTime = findViewById(R.id.sundayTimeText);
        mondayTime = findViewById(R.id.mondayTimeText);
        tuesdayTime = findViewById(R.id.tuesdayTimeText);
        wednesdayTime = findViewById(R.id.wednesdayTimeText);
        thursdayTime = findViewById(R.id.thursdayTimeText);
        fridayTime = findViewById(R.id.fridayTimeText);
        saturdayTime = findViewById(R.id.saturdayTimeText);

        loadHours();

    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
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

}