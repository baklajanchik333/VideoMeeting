package com.example.videomeeting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeeting.R;
import com.example.videomeeting.utilities.Constants;

public class IncomingInvitationActivity extends AppCompatActivity {
    private ImageView imageMeetingType;
    private TextView textFirstChar, textUserName, textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);

        initialization();

        String firstName = getIntent().getStringExtra(Constants.KEY_FIRST_NAME);
        if (firstName != null) {
            textFirstChar.setText(firstName.substring(0, 1));
        }
        textUserName.setText(String.format("%s %s", firstName, getIntent().getStringExtra(Constants.KEY_LAST_NAME)));
        textEmail.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));

        String meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_videocam);
            }
        }
    }

    private void initialization() {
        imageMeetingType = findViewById(R.id.imageMeetingType);
        textFirstChar = findViewById(R.id.textFirstChar);
        textUserName = findViewById(R.id.textUserName);
        textEmail = findViewById(R.id.textEmail);
    }
}