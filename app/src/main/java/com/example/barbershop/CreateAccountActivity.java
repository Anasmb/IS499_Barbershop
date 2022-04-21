package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class CreateAccountActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextInputEditText name, email, password, phoneNumber;
    private MaterialButton signupButton;
    private TextView phoneNumberWarning, emailWarning, passwordWarning, nameWarning;
    private boolean isNameValid , isPhoneNumberValid , isEmailValid , isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        backBtn = findViewById(R.id.createAccount_back_button);
        backBtn.setOnClickListener(backBtnListener);

        name = findViewById(R.id.createAccountNameEditText);
        name.addTextChangedListener(nameInputCheck);
        email = findViewById(R.id.createAccountEmailEditText);
        email.addTextChangedListener(emailInputCheck);
        password = findViewById(R.id.createAccountPasswordEditText);
        password.addTextChangedListener(passwordInputCheck);
        phoneNumber = findViewById(R.id.createAccountPhoneNumberEditText);
        phoneNumber.addTextChangedListener(phoneNumberInputCheck);

        signupButton = findViewById(R.id.createAccountSignupBtn);
        signupButton.setOnClickListener(signupListener);

        phoneNumberWarning = findViewById(R.id.mobileNumberWarning);
        emailWarning = findViewById(R.id.emailWarning);
        passwordWarning = findViewById(R.id.passwordWarning);
        nameWarning = findViewById(R.id.nameWarning);

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

            if(isNameValid && isEmailValid && isPhoneNumberValid && isPasswordValid) {

            signupButton.setClickable(false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[4];
                    field[0] = "name";
                    field[1] = "email";
                    field[2] = "password";
                    field[3] = "phonenumber";

                    String[] data = new String[4];
                    data[0] = String.valueOf(name.getText());
                    data[1] = String.valueOf(email.getText());
                    data[2] = String.valueOf(password.getText());
                    data[3] = String.valueOf(phoneNumber.getText());
                    Log.d("php" , data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
                    PutData putData = new PutData("http://192.168.100.6/barbershop-php/customer/signup.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if(result.equals("Sign Up Success")){
                                Toast.makeText(getApplicationContext(),"Register Success",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Unable to connect to Database", Toast.LENGTH_SHORT).show();
                                Log.d("php", result);
                                signupButton.setClickable(true);
                            }
                        }
                    }

                }
            });
        }

          }
    };


    private TextWatcher nameInputCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() < 1){
                nameWarning.setVisibility(View.VISIBLE);
                isNameValid = false;
            }
            else{
                isNameValid = true;
                nameWarning.setVisibility(View.INVISIBLE);
            }
        }
    };

    private TextWatcher emailInputCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()){ // check the email format, is it valid or not?
                emailWarning.setVisibility(View.VISIBLE);
                isEmailValid = false;
            }
            else {
                emailWarning.setVisibility(View.INVISIBLE);
                isEmailValid = true;
            }
        }
    };

    private TextWatcher phoneNumberInputCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() != 10){
                phoneNumberWarning.setVisibility(View.VISIBLE);
                isPhoneNumberValid = false;
            }
            else {
                phoneNumberWarning.setVisibility(View.INVISIBLE);
                isPhoneNumberValid = true;
            }
        }
    };

    private TextWatcher passwordInputCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() < 8){
                passwordWarning.setVisibility(View.VISIBLE);
                isPasswordValid = false;
            }
            else {
                passwordWarning.setVisibility(View.INVISIBLE);
                isPasswordValid = true;
            }
        }
    };

}