package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        backBtn = findViewById(R.id.changePassword_back_button);
        backBtn.setOnClickListener(backBtnListener);
    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}