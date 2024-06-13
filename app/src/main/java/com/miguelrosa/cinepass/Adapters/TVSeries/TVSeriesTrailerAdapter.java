package com.miguelrosa.cinepass.Adapters.TVSeries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miguelrosa.cinepass.Activities.TVSeriesDetailActivity;
import com.miguelrosa.cinepass.Domain.Models.Video;
import com.miguelrosa.cinepass.R;

import java.util.List;

public class TVSeriesTrailerAdapter extends RecyclerView.Adapter<TVSeriesTrailerAdapter.TVSeriesTrailerViewHolder> {
    private List<Video> trailers;
    private Context context;

    public TVSeriesTrailerAdapter(List<Video> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
    }

    @NonNull
    @Override
    public TVSeriesTrailerAdapter.TVSeriesTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new TVSeriesTrailerAdapter.TVSeriesTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVSeriesTrailerAdapter.TVSeriesTrailerViewHolder holder, int position) {
        Video trailer = trailers.get(position);
        String thumbnailUrl = "https://img.youtube.com/vi/" + trailer.getKey() + "/hqdefault.jpg";
        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof TVSeriesDetailActivity) {
                ((TVSeriesDetailActivity) context).onTrailerClicked("https://www.youtube.com/embed/" + trailer.getKey() + "?autoplay=1&fs=1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public static class TVSeriesTrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        public TVSeriesTrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    public interface TrailerClickListener {
        void onTrailerClicked(String videoUrl);
    }
}
