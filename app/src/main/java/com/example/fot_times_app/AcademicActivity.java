package com.example.fot_times_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AcademicActivity extends AppCompatActivity {

    EditText searchInput;
    RecyclerView recyclerViewNews;
    NewsAdapter adapter;
    List<NewsItem> academicList = new ArrayList<>();
    String loggedUsername;

    DatabaseReference academicRef = FirebaseDatabase.getInstance().getReference("academic");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        //  Get the logged-in username from Intent
        loggedUsername = getIntent().getStringExtra("username");
        if (loggedUsername == null) {
            Toast.makeText(this, "User not found. Returning to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        searchInput = findViewById(R.id.searchInput);
        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(new ArrayList<>());
        recyclerViewNews.setAdapter(adapter);

        //  Load Academic News from Firebase
        academicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                academicList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NewsItem item = data.getValue(NewsItem.class);
                    if (item != null) {
                        int imageId = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
                        item.setImageResId(imageId);
                        academicList.add(item);
                    }
                }
                adapter.updateList(academicList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AcademicActivity.this, "Failed to load academic news", Toast.LENGTH_SHORT).show();
            }
        });

        //  Enable Search Filtering
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        //  Settings Icon → DeveloperInfoActivity
        findViewById(R.id.settingsIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, DeveloperInfoActivity.class);
            intent.putExtra("username", loggedUsername);
            intent.putExtra("source", "academic"); // Indicate source
            startActivity(intent);
        });

        //  Profile Icon → UserInfoActivity
        findViewById(R.id.profileIcon).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra("username", loggedUsername);
            startActivity(intent);
        });

        //  Bottom Navigation handling
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_academic);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sports) {
                Intent intent = new Intent(this, NewsActivity.class);
                intent.putExtra("username", loggedUsername);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_events) {
                Intent intent = new Intent(this, EventsActivity.class);
                intent.putExtra("username", loggedUsername);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_academic) {
                return true; // Already on this section
            }
            return false;
        });
    }

    private void filterNews(String query) {
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : academicList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getSummary().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }
}