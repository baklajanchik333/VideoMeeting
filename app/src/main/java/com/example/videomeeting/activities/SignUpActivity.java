package com.example.videomeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videomeeting.R;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ImageView imageBack;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;
    private MaterialButton signUpBtn;
    private TextView textSingIn;
    private ProgressBar signUpProgressBar;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();

        imageBack.setOnClickListener(v -> onBackPressed());

        textSingIn.setOnClickListener(v -> onBackPressed());

        signUpBtn.setOnClickListener(v -> inputData());
    }

    private void inputData() {
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter first name...", Toast.LENGTH_SHORT).show();
        } else if (inputLastName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter last name...", Toast.LENGTH_SHORT).show();
        } else if (inputEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter email...", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter valid email...", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show();
        } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Confirm your password...", Toast.LENGTH_SHORT).show();
        } else if (!inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())) {
            Toast.makeText(this, "Password & confirm password must be same...", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "Пароль должен содержать не меньше 6 символов...", Toast.LENGTH_SHORT).show();
        } else {
            signUp();
        }
    }

    private void signUp() {
        signUpBtn.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString().trim());
        hashMap.put(Constants.KEY_LAST_NAME, inputLastName.getText().toString().trim());
        hashMap.put(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());
        hashMap.put(Constants.KEY_PASSWORD, inputPassword.getText().toString().trim());

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).add(hashMap)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    signUpBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initialization() {
        imageBack = findViewById(R.id.imageBack);
        textSingIn = findViewById(R.id.textSingIn);
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        preferenceManager = new PreferenceManager(getApplicationContext());
    }
}