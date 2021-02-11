package com.example.videomeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videomeeting.R;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private MaterialButton signInBtn;
    private ProgressBar signUpProgressBar;
    private TextView textSingUp;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialization();

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        signInBtn.setOnClickListener(v -> inputData());

        textSingUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void inputData() {
        if (inputEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter email...", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter valid email...", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show();
        } else {
            signIn();
        }
    }

    private void signIn() {
        signInBtn.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, inputEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        signInBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SignInActivity.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initialization() {
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        signInBtn = findViewById(R.id.signInBtn);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        textSingUp = findViewById(R.id.textSingUp);

        preferenceManager = new PreferenceManager(getApplicationContext());
    }
}