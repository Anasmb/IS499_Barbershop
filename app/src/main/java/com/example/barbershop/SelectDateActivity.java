package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.barbershop.items.ServiceItem;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Calendar myCalendar = Calendar.getInstance();
    private ImageView backBtn;
    private ArrayList<ServiceItem> selectedServices;
    private int shopID;
    private String barberName;
    private TextInputEditText dateText,timeText;
    private LinearLayout atBarbershop, atHouse;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        selectedServices = getIntent().getExtras().getParcelableArrayList("services");
        shopID = getIntent().getExtras().getInt("barbershopID");
        barberName = getIntent().getExtras().getString("barberName");
        shopName = getIntent().getExtras().getString("shopName");

        backBtn = findViewById(R.id.selectDate_backButton);
        backBtn.setOnClickListener(backBtnListener);
        dateText = findViewById(R.id.selectDateEditText);
        dateText.setOnClickListener(dateClickListener);
        timeText = findViewById(R.id.selectTimeEditText);
        timeText.setOnClickListener(timeClickListener);

        atBarbershop = findViewById(R.id.atBarbershopLayout);
        atBarbershop.setOnClickListener(barbershopLayoutListener);
        atHouse = findViewById(R.id.yourHouseLayout);
        atHouse.setOnClickListener(houseLayoutListener);

    }

    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(SelectDateActivity.this,SelectDateActivity.this::onDateSet,myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // block past days to be selected
            datePickerDialog.show();
        }
    };

    private View.OnClickListener timeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(SelectDateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    String AM_PM ;
                    int hour = hourOfDay;
                    if(hour > 12) {
                        hour -= 12;
                        AM_PM = "PM";
                    } else if(hour == 0) {
                        hour += 12;
                        AM_PM = "AM";
                    }
                    else if(hour == 12){
                        AM_PM = "PM";
                    }
                    else {
                        AM_PM = "AM";
                    }
                    timeText.setText(String.format("%02d:%02d", hour,minute) + " " + AM_PM);
                }
            },12,0,false);
            timePickerDialog.show();
        }
    };

    private View.OnClickListener barbershopLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (timeText.length() > 0 && dateText.length() > 0) { // check if time and date are not empty
                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                intent.putExtra("barbershopID", shopID);
                intent.putExtra("barberName", barberName);
                intent.putExtra("date", dateText.getText().toString());
                intent.putExtra("time", timeText.getText().toString());
                intent.putExtra("shopName",shopName);
                intent.putParcelableArrayListExtra("services", selectedServices);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener houseLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(timeText.length() > 0 && dateText.length() > 0) { // check if time and date are not empty
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("barbershopID", shopID);
                intent.putExtra("barberName", barberName);
                intent.putExtra("date", dateText.getText().toString());
                intent.putExtra("time", timeText.getText().toString());
                intent.putExtra("shopName",shopName);
                intent.putParcelableArrayListExtra("services", selectedServices);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        dateText.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" "));
    }


}