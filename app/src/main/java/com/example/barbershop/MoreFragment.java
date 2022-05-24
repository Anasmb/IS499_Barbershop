package com.example.barbershop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {

    private LinearLayout myAccountLayout,changeLanguageLayout,customerSupportLayout,signInLayout;
    private TextView customerName,signInText;
    private SharedPreferences preferences;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        myAccountLayout = view.findViewById(R.id.myAccountLayout);
        myAccountLayout.setOnClickListener(myAccountLayoutListener);
        changeLanguageLayout = view.findViewById(R.id.changeLanguageLayout);
        changeLanguageLayout.setOnClickListener(changeLanguageLayoutListener);
        customerSupportLayout = view.findViewById(R.id.customerSupportLayout);
        customerSupportLayout.setOnClickListener(customerSupportLayoutListener);
        signInLayout = view.findViewById(R.id.signInLayout);
        signInLayout.setOnClickListener(signInLayoutListener);

        customerName = view.findViewById(R.id.moreCustomerName);
        signInText = view.findViewById(R.id.signInTxt);

        if(!preferences.getString("name", "").isEmpty()){
            customerName.setText("Welcome " + preferences.getString("name",""));
            signInText.setText("Logout");
        }

        return view;
    }

    private View.OnClickListener myAccountLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(preferences.getString("customerID", "").isEmpty()){
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }

        }
    };


    private View.OnClickListener changeLanguageLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener customerSupportLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener signInLayoutListener = new View.OnClickListener() { //TODO FIX BACK ISSUE, after take an appointment and then logout  then press back it takes you to the wrong page
        @Override
        public void onClick(View view) {
            if (preferences.getString("customerID", "").isEmpty()){
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
            }
            else { // delete customer info from local storage application
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity().getApplicationContext() , SigninActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

        }
    };

}
