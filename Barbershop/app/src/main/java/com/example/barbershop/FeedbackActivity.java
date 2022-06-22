package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershop.adapters.FeedbackAdapter;
import com.example.barbershop.items.FeedbackItem;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/feedback/getFeedback.php";
    private ImageView backBtn,addFeedbackBtn;
    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<FeedbackItem> feedbackItemList;
    private int shopID;
    private SharedPreferences preferences;
    private boolean isRated,didBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        shopID = getIntent().getExtras().getInt("barbershopID");
        SQL_URL += "?barbershopID=" + shopID;

        feedbackItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.feedbackRecyclerView);
        recyclerView.setHasFixedSize(true);

        backBtn = findViewById(R.id.feedback_backButton);
        backBtn.setOnClickListener(backBtnListener);
        addFeedbackBtn = findViewById(R.id.addFeedbackButton);
        addFeedbackBtn.setOnClickListener(addFeedbackListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadFeedback();

    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener addFeedbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!preferences.getString("customerID","").isEmpty()){
                if(isRated == false && didBook == true){
                    Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                    intent.putExtra("barbershopID", shopID);
                    startActivity(intent);
                }
                else if (isRated == true){
                    Toast.makeText(getApplicationContext(),"You already rated this shop" , Toast.LENGTH_LONG).show();
                }
                else if(didBook == false){
                    Toast.makeText(getApplicationContext(),"You must try this shop to give feedback" , Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Please login first" , Toast.LENGTH_LONG).show();
            }

        }
    };


    private void loadFeedback(){
        isRated();
        didBookPreviously();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("php", response);
                try {
                    JSONArray feedbacks = new JSONArray(response);
                    for (int i = 0 ; i < feedbacks.length() ; i++){
                        JSONObject feedbackObject = feedbacks.getJSONObject(i);
                        String comment = feedbackObject.getString("comment");
                        int stars = feedbackObject.getInt("stars");
                        FeedbackItem feedbackItem = new FeedbackItem(comment,stars);
                        feedbackItemList.add(feedbackItem);
                    }

                    adapter = new FeedbackAdapter(getApplicationContext(), feedbackItemList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // this method will execute if there is error
                Log.d("php", "onErrorResponse: " + error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void isRated(){ // prevent customer to rate the barbershop more than once
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "barbershopID";
                field[1] = "customerID";
                String[] data = new String[2];
                data[0] = String.valueOf(shopID);
                data[1] = preferences.getString("customerID","");
                PutData putData = new PutData("http://192.168.100.6/barbershop-php/feedback/checkFeedback.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.d("php", result);
                        if (result.equals("true")){
                            isRated = true;
                        }
                        else if(result.equals("false")) {
                            isRated = false;
                        }
                    }
                }

            }
        });
    }

    private void didBookPreviously(){ // customer cannot rate if he didn't try the barbershop
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "barbershopID";
                field[1] = "customerID";
                String[] data = new String[2];
                data[0] = String.valueOf(shopID);
                data[1] = preferences.getString("customerID","");
                PutData putData = new PutData("http://192.168.100.6/barbershop-php/appointment/getAppointment.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.d("php", result);
                        if (result.equals("true")){
                            didBook = true;
                        }
                        else if(result.equals("false")) {
                            didBook = false;
                        }
                    }
                }

            }
        });
    }
}