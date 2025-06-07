package com.example.fot_times_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private Context context;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);

        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary()); // shown in card, but not used in detail screen
        holder.date.setText(item.getDate());
        holder.newsImage.setImageResource(item.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("image", item.getImage());
            intent.putExtra("description", item.getDescription()); // ✅ Pass full description
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateList(List<NewsItem> updatedList) {
        this.newsList = updatedList;
        notifyDataSetChanged();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, summary, date;
        ImageView newsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            summary = itemView.findViewById(R.id.newsSummary);
            date = itemView.findViewById(R.id.newsDate);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }
}
