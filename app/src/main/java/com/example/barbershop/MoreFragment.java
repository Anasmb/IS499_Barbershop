package com.example.barbershop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {

    private LinearLayout myAccountLayout;
    private LinearLayout changePasswordLayout;
    private LinearLayout changeLanguageLayout;
    private LinearLayout customerSupportLayout;
    private LinearLayout signInLayout;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        myAccountLayout = view.findViewById(R.id.myAccountLayout);
        myAccountLayout.setOnClickListener(myAccountLayoutListener);

        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        changePasswordLayout.setOnClickListener(changeLanguageLayoutListener);

        changeLanguageLayout = view.findViewById(R.id.changeLanguageLayout);
        changeLanguageLayout.setOnClickListener(changeLanguageLayoutListener);

        customerSupportLayout = view.findViewById(R.id.customerSupportLayout);
        customerSupportLayout.setOnClickListener(customerSupportLayoutListener);

        signInLayout = view.findViewById(R.id.signInLayout);
        signInLayout.setOnClickListener(signInLayoutListener);

        return view;
    }

    private View.OnClickListener myAccountLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener changePasswordLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

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

    private View.OnClickListener signInLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    

}
