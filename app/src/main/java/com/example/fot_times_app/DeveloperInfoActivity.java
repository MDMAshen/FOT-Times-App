package com.example.fot_times_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeveloperInfoActivity extends AppCompatActivity {

    Button exitButton;
    String username;
    String source; // sports, academic, events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        //  Get username and source safely
        username = getIntent().getStringExtra("username");
        source = getIntent().getStringExtra("source");

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            if (source == null || username == null) {
                // If either is missing, go safely to Login
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            //  Go back to correct section
            Intent intent;
            switch (source) {
                case "sports":
                    intent = new Intent(this, NewsActivity.class);
                    break;
                case "academic":
                    intent = new Intent(this, AcademicActivity.class);
                    break;
                case "events":
                    intent = new Intent(this, EventsActivity.class);
                    break;
                default:
                    intent = new Intent(this, LoginActivity.class);
                    break;
            }

            //  Pass username back
            intent.putExtra("username", username);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
