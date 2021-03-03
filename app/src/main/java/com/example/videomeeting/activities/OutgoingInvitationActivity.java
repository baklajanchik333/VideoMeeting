package com.example.videomeeting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeeting.R;
import com.example.videomeeting.models.User;

public class OutgoingInvitationActivity extends AppCompatActivity {
    private ImageView imageMeetingType, imageStopInvitation;
    private TextView textFirstChar, textUserName, textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);

        imageMeetingType = findViewById(R.id.imageMeetingType);
        textFirstChar = findViewById(R.id.textFirstChar);
        textUserName = findViewById(R.id.textUserName);
        textEmail = findViewById(R.id.textEmail);
        imageStopInvitation = findViewById(R.id.imageStopInvitation);

        String meetingType = getIntent().getStringExtra("type");

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_videocam);
            }
        }

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            textFirstChar.setText(user.firstName.substring(0, 1));
            textUserName.setText(String.format("%s %s", user.firstName, user.lastName));
            textEmail.setText(user.email);
        }

        imageStopInvitation.setOnClickListener(v -> onBackPressed());
    }
}