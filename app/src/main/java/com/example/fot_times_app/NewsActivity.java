package com.example.fot_times_app;

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
    NewsAdapter newsAdapter;
    TrendingAdapter trendingAdapter;
    List<NewsItem> newsList = new ArrayList<>();
    List<TrendingItem> trendingList = new ArrayList<>();
    EditText searchInput;

    DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("news");
    DatabaseReference trendingRef = FirebaseDatabase.getInstance().getReference("trending");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(new ArrayList<>());
        recyclerViewNews.setAdapter(newsAdapter);

        recyclerViewTrending = findViewById(R.id.recyclerViewTrending);
        recyclerViewTrending.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingAdapter = new TrendingAdapter(trendingList, this);
        recyclerViewTrending.setAdapter(trendingAdapter);

        searchInput = findViewById(R.id.searchInput);

        loadNewsFromFirebase();
        loadTrendingFromFirebase();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_sports);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sports) return true;
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            return true;
        });

        findViewById(R.id.profileIcon).setOnClickListener(v ->
                Toast.makeText(this, "Profile - Coming soon", Toast.LENGTH_SHORT).show());
        findViewById(R.id.settingsIcon).setOnClickListener(v ->
                Toast.makeText(this, "Settings - Coming soon", Toast.LENGTH_SHORT).show());

        // 🔍 Live Search
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
                newsAdapter.updateList(newsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrendingFromFirebase() {
        trendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trendingList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TrendingItem item = data.getValue(TrendingItem.class);
                    if (item != null) {
                        int imageId = getResources().getIdentifier(item.getImage(), "drawable", getPackageName());
                        item.setImageResId(imageId);
                        trendingList.add(item);
                    }
                }
                trendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsActivity.this, "Failed to load trending", Toast.LENGTH_SHORT).show();
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
        newsAdapter.updateList(filtered);
    }
}
