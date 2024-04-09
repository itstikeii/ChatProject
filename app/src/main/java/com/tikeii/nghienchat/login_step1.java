package com.tikeii.nghienchat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.hbb20.CountryCodePicker;

public class login_step1 extends AppCompatActivity {

    CountryCodePicker mavung;
    EditText sdt;
    ImageButton tiep;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step1);
        progressBar = findViewById(R.id.login_stp1_progressBar); progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminateDrawable(new FadingCircle());

        mavung = findViewById(R.id.login_stp1_countryCode);
        sdt = findViewById(R.id.login_stp1_phone);
        tiep = findViewById(R.id.login_stp1_next);

        tiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mavung.registerCarrierNumberEditText(sdt);
             if (mavung.isValidFullNumber()==false) {
                 sdt.setError("Có lỗi xãy ra vui lòng nhập lại !");
                 return;
             }

             Intent i = new Intent(getApplicationContext(), login_step2.class);
             i.putExtra("PHONE_INPUT",mavung.getFullNumberWithPlus());
             startActivity(i);
            }
        });
    }
}