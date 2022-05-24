package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MyAccountActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextInputEditText name , email, phoneNumber ,password;
    private SharedPreferences preferences;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        backBtn = findViewById(R.id.myAccount_back_button);
        backBtn.setOnClickListener(backBtnListener);
        saveButton = findViewById(R.id.myAccountSaveButton);
        saveButton.setOnClickListener(saveButtonListener);

        name = findViewById(R.id.myAccountNameEditText);
        email = findViewById(R.id.myAccountEmailEditText);
        phoneNumber = findViewById(R.id.myAccountNumberEditText);
        password = findViewById(R.id.myAccountPasswordEditText);

        loadAccountInformation();
    }

    private void loadAccountInformation(){
        name.setText(preferences.getString("name" , ""));
        email.setText(preferences.getString("email",""));
        phoneNumber.setText(preferences.getString("phoneNumber" , ""));
        password.setText(preferences.getString("password", ""));
    }


    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (name.length() > 0 && email.length() > 0 && phoneNumber.length() > 9 && password.length() > 7) {

                saveButton.setClickable(false);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[5];
                        field[0] = "name";
                        field[1] = "email";
                        field[2] = "phoneNumber";
                        field[3] = "password";
                        field[4] = "customerID";

                        String[] data = new String[5];
                        data[0] = String.valueOf(name.getText());
                        data[1] = String.valueOf(email.getText());
                        data[2] = String.valueOf(phoneNumber.getText());
                        data[3] = String.valueOf(password.getText());
                        data[4] = preferences.getString("customerID", "");
                        PutData putData = new PutData("http://192.168.100.6/barbershop-php/customer/updateCustomerInfo.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Update Success")) {
                                    Log.d("php", result);
                                    Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                                    updateLocalInfo();
                                    finish();
                                } else { // All fields are required
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    Log.d("php", result);
                                    saveButton.setClickable(true);
                                }
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields are required !", Toast.LENGTH_LONG).show();
            }

        }
    };

    private void updateLocalInfo(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name" , String.valueOf(name.getText()));
        editor.putString("email" , String.valueOf(email.getText()));
        editor.putString("password" , String.valueOf(phoneNumber.getText()));
        editor.putString("phoneNumber" , String.valueOf(password.getText()));
        editor.apply();
    }

}