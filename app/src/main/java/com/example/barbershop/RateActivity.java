package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RateActivity extends AppCompatActivity {

    private ImageView backBtn;
    private MaterialButton clickedRate,submitBtn ,rate1,rate2,rate3,rate4,rate5;
    private EditText feedback;
    private SharedPreferences preferences;
    private int barbershopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        preferences = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        barbershopID = getIntent().getExtras().getInt("barbershopID");

        backBtn = findViewById(R.id.rate_backButton);
        backBtn.setOnClickListener(backBtnListener);
        submitBtn = findViewById(R.id.rateSubmitButton);
        submitBtn.setOnClickListener(submitListener);

        rate1 = findViewById(R.id.rateOneButton);
        rate1.setOnClickListener(rateListener);
        rate2 = findViewById(R.id.rateTwoButton);
        rate2.setOnClickListener(rateListener);
        rate3 = findViewById(R.id.rateThreeButton);
        rate3.setOnClickListener(rateListener);
        rate4 = findViewById(R.id.rateFourButton);
        rate4.setOnClickListener(rateListener);
        rate5 = findViewById(R.id.rateFiveButton);
        rate5.setOnClickListener(rateListener);

        feedback = findViewById(R.id.feedbackEditText);

    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener submitListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (feedback.getText().toString().length() > 3){ // Check if user added feedback or not
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[4];
                        field[0] = "stars";
                        field[1] = "comment";
                        field[2] = "barbershopID";
                        field[3] = "customerID";
                        String[] data = new String[4];
                        data[0] = clickedRate.getText().toString();
                        data[1] = feedback.getText().toString();
                        data[2] = String.valueOf(barbershopID);
                        data[3] = preferences.getString("customerID","");
                        PutData putData = new PutData("http://188.54.243.108/barbershop-php/feedback/addFeedback.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.d("php", result);
                                Toast.makeText(getApplicationContext(),"Thank you for your feedback" , Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(),"Please add feedback" , Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener rateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(clickedRate != null){
                clickedRate.setBackgroundColor(getResources().getColor(R.color.background_light_blue));
            }
            clickedRate = (MaterialButton) view;
            view.setBackgroundColor(Color.parseColor("#FFCC00"));
        }
    };


}