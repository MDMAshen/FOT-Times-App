package com.example.fot_times_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    RecyclerView recyclerViewNews, recyclerViewTrending;
    NewsAdapter adapter;
    TrendingAdapter trendingAdapter;
    List<NewsItem> newsList;
    List<TrendingItem> trendingList;
    EditText searchInput;

    String loggedUsername;

    DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("news");
    DatabaseReference trendingRef = FirebaseDatabase.getInstance().getReference("trending");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //  Get logged-in username
        loggedUsername = getIntent().getStringExtra("username");
        if (loggedUsername == null) {
            Toast.makeText(this, "User not found. Returning to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewTrending = findViewById(R.id.recyclerViewTrending);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        searchInput = findViewById(R.id.searchInput);

        newsList = new ArrayList<>();
        trendingList = new ArrayList<>();

        adapter = new NewsAdapter(new ArrayList<>());
        recyclerViewNews.setAdapter(adapter);

        trendingAdapter = new TrendingAdapter(trendingList, this);
        recyclerViewTrending.setAdapter(trendingAdapter);

        loadNewsFromFirebase();
        loadTrendingFromFirebase();

        //  Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_sports);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sports) {
                return true; // Already on sports
            } else if (id == R.id.nav_academic) {
                Intent academicIntent = new Intent(this, AcademicActivity.class);
                academicIntent.putExtra("username", loggedUsername);
                //  Prevent stack buildup
                academicIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(academicIntent);
                finish();
                return true;
            } else if (id == R.id.nav_events) {
                Intent eventsIntent = new Intent(this, EventsActivity.class);
                eventsIntent.putExtra("username", loggedUsername);
                eventsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(eventsIntent);
                finish();
                return true;
            }
            return false;
        });

        //  Top icons
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(NewsActivity.this, UserInfoActivity.class);
            intent.putExtra("username", loggedUsername);
            startActivity(intent);
        });

        settingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(NewsActivity.this, DeveloperInfoActivity.class);
            intent.putExtra("username", loggedUsername);
            intent.putExtra("source", "sports"); //  Pass current section
            startActivity(intent);
        });

        //  Search functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadNewsFromFirebase() {
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NewsItem item = data.getValue(NewsItem.class);
                    if (item != null) {
                        int imageId = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
                        item.setImageResId(imageId);
                        newsList.add(item);
                    }
                }
                adapter.updateList(newsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrendingFromFirebase() {
        trendingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trendingList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TrendingItem item = data.getValue(TrendingItem.class);
                    if (item != null && item.getImage() != null) {
                        int resId = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
                        item.setImageResId(resId);
                        trendingList.add(item);
                    }
                }
                trendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsActivity.this, "Failed to load trending news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterNews(String query) {
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : newsList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getSummary().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }
}
