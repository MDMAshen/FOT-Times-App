package com.example.fot_times_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {

    private final List<TrendingItem> trendingList;
    private final Context context;

    public TrendingAdapter(List<TrendingItem> trendingList, Context context) {
        this.trendingList = trendingList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_card, parent, false);
        return new TrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        TrendingItem item = trendingList.get(position);
        int imageResId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
        holder.trendingImage.setImageResource(imageResId);

        holder.trendingImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("image", item.getImage());
            intent.putExtra("description", item.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }

    public static class TrendingViewHolder extends RecyclerView.ViewHolder {
        ImageView trendingImage;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            trendingImage = itemView.findViewById(R.id.trendingCardImage);
        }
    }
}
