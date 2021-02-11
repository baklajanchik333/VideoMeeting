package com.example.videomeeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videomeeting.R;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView textTitle, textSignOut;
    private RecyclerView usersRecyclerView;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        textTitle.setText(String.format("%s %s",
                preferenceManager.getString(Constants.KEY_FIRST_NAME),
                preferenceManager.getString(Constants.KEY_LAST_NAME)));

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        sendFCMTokenToDatabase(task.getResult().getToken());
                    }
                });

        textSignOut.setOnClickListener(v -> signOut());
    }

    private void sendFCMTokenToDatabase(String token) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Token updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to send token: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void signOut() {
        Toast.makeText(this, "SigningOut...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(hashMap)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }

    private void initialization() {
        textTitle = findViewById(R.id.textTitle);
        textSignOut = findViewById(R.id.textSignOut);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        preferenceManager = new PreferenceManager(getApplicationContext());
    }
}