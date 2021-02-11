package com.example.videomeeting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeeting.R;

public class SignUpActivity extends AppCompatActivity {
    private ImageView imageBack;
    private TextView textSingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();

        imageBack.setOnClickListener(v -> onBackPressed());

        textSingIn.setOnClickListener(v -> onBackPressed());
    }

    private void initialization() {
        imageBack = findViewById(R.id.imageBack);
        textSingIn = findViewById(R.id.textSingIn);
    }
}