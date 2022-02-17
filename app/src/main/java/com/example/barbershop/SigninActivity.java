package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SigninActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private MaterialButton signinButton;
    private ImageView backBtn;
    private TextView signupTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.signinEmailEditText);
        password = findViewById(R.id.signinPasswordEditText);

        signinButton = findViewById(R.id.signinButton);
        signinButton.setOnClickListener(signinListener);

        backBtn = findViewById(R.id.signin_back_button);
        backBtn.setOnClickListener(backBtnListener);
        signupTxt = findViewById(R.id.signupTxt);
        signupTxt.setOnClickListener(signupListener);
    }

    private View.OnClickListener signinListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(email.getText().length() > 0 && password.getText().length() > 0) {
                //Start ProgressBar first (Set visibility VISIBLE)
                signinButton.setClickable(false);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[2];
                        field[0] = "email";
                        field[1] = "password";
                        //Creating array for data
                        String[] data = new String[2];
                        data[0] = String.valueOf(email.getText());
                        data[1] = String.valueOf(password.getText());
                        Log.d("php" , data[0] + " " + data[1]);
                        PutData putData = new PutData("http://192.168.100.6/barbershop-php/login.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Login Success")){
                                    Log.d("php", result);
                                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                                    Log.d("php", result);
                                    signinButton.setClickable(true);
                                }
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }


        }
    };

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


}