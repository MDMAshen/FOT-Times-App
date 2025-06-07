package com.example.fot_times_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView detailImage, backIcon;
    TextView detailTitle, detailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        backIcon = findViewById(R.id.backIcon); // 🔙 back button

        String title = getIntent().getStringExtra("title");
        String imageName = getIntent().getStringExtra("image");
        String description = getIntent().getStringExtra("description");

        if (title == null || imageName == null || description == null) {
            Toast.makeText(this, "News not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        detailTitle.setText(title);
        detailDescription.setText(description);

        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageResId != 0) {
            detailImage.setImageResource(imageResId);
        } else {
            detailImage.setImageResource(R.drawable.placeholder_image);
        }

        backIcon.setOnClickListener(v -> finish()); //  Go back on click
    }
}
