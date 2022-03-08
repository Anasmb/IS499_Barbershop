package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopPageActivity extends AppCompatActivity {

    private TextView shopName;
    private int shopID;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        backBtn = findViewById(R.id.shopPage_backButton);
        backBtn.setOnClickListener(backBtnListener);
        shopName = findViewById(R.id.shopPage_salonNameText);

        shopID = getIntent().getExtras().getInt("barbershopID");
        shopName.setText(getIntent().getExtras().getString("shopName"));

    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}