package com.example.fot_times_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.summary.setText(item.getSummary());
        holder.date.setText(item.getDate());

        //  Safely load image using pre-resolved resource ID
        holder.newsImage.setImageResource(item.getImageResId());

        //  Show toast on click
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    //  Helper method to update list (used in search)
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
            summary = itemView.findViewById(R.id.newsSummary); //  correct ID from news_card.xml
            date = itemView.findViewById(R.id.newsDate);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }
}
