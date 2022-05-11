package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity extends AppCompatActivity {

    private String SQL_URL = "http://192.168.100.6/barbershop-php/customer/getCustomerInfo.php";
    private TextInputEditText phonenumber, password;
    private MaterialButton signinButton;
    private ImageView backBtn;
    private TextView signupTxt;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        viewsInitialization();
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    private View.OnClickListener signinListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(phonenumber.getText().length() > 0 && password.getText().length() > 0) {
                signinButton.setClickable(false);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[2];
                        field[0] = "phonenumber";
                        field[1] = "password";

                        String[] data = new String[2];
                        data[0] = String.valueOf(phonenumber.getText());
                        data[1] = String.valueOf(password.getText());
                        Log.d("php" , data[0] + " " + data[1]);
                        PutData putData = new PutData("http://192.168.100.6/barbershop-php/customer/loginCustomer.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Login Success")){
                                    Log.d("php", result);
                                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                                    SQL_URL += "?phoneNumber=" + String.valueOf(phonenumber.getText());
                                    saveCustomerInfo();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Unable to connect to Database", Toast.LENGTH_SHORT).show();
                                    Log.d("php", result);
                                    signinButton.setClickable(true);
                                }
                            }
                        }
                    }
                });
            }


        }
    };

    private void saveCustomerInfo(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SQL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SharedPreferences.Editor editor = preferences.edit();
                    JSONArray customer = new JSONArray(response);
                    for (int i = 0 ; i < customer.length() ; i++){
                        JSONObject customerObject = customer.getJSONObject(i);
                        editor.putString("customerID" , customerObject.getString("customerID"));
                        editor.putString("name" , customerObject.getString("name"));
                        editor.putString("email" , customerObject.getString("email"));
                        editor.putString("password" , customerObject.getString("password"));
                        editor.putString("phoneNumber" , String.valueOf(phonenumber.getText()));
                        editor.apply();
                    }
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
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

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener signupListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivity(intent);
        }
    };

    private void viewsInitialization(){
        phonenumber = findViewById(R.id.signinPhoneNumberEditText);
        password = findViewById(R.id.signinPasswordEditText);

        signinButton = findViewById(R.id.signinButton);
        signinButton.setOnClickListener(signinListener);

        backBtn = findViewById(R.id.signin_back_button);
        backBtn.setOnClickListener(backBtnListener);
        signupTxt = findViewById(R.id.signupTxt);
        signupTxt.setOnClickListener(signupListener);
    }


}