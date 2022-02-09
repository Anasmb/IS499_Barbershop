package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SigninActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView signupTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        backBtn = findViewById(R.id.signin_back_button);
        backBtn.setOnClickListener(backBtnListener);
        signupTxt = findViewById(R.id.signupTxt);
        signupTxt.setOnClickListener(signupListener);
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


}