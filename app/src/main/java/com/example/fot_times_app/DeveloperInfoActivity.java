package com.example.fot_times_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperInfoActivity extends AppCompatActivity {

    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        // 🔴 Don't reference backIcon since it was removed
        // ImageView backIcon = findViewById(R.id.backIcon);
        // backIcon.setOnClickListener(v -> finish());

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            // Navigate back to NewsActivity
            Intent intent = new Intent(DeveloperInfoActivity.this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
