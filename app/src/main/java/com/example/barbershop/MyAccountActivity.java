package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyAccountActivity extends AppCompatActivity {

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        backBtn = findViewById(R.id.myAccount_back_button);
        backBtn.setOnClickListener(backBtnListener);
    }


    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}