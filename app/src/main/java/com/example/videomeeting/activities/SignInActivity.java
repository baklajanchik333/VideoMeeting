package com.example.videomeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videomeeting.R;

public class SignInActivity extends AppCompatActivity {
    private TextView textSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialization();

        textSingUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void initialization() {
        textSingUp = findViewById(R.id.textSingUp);
    }
}