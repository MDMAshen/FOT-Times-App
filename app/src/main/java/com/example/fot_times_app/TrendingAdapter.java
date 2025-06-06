package com.example.fot_times_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
        int resId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(resId);

        holder.imageView.setOnClickListener(v ->
                Toast.makeText(context, item.getTitle() + " - Coming soon", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }

    static class TrendingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trendingCardImage);
        }
    }
}
