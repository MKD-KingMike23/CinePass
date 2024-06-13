package com.miguelrosa.cinepass.Adapters.TVSeries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.miguelrosa.cinepass.Activities.TVSeriesDetailActivity;
import com.miguelrosa.cinepass.Domain.Models.TVSeries;
import com.miguelrosa.cinepass.R;

import java.util.ArrayList;
import java.util.List;

public class TVSeriesSearchAdapter extends RecyclerView.Adapter<TVSeriesSearchAdapter.TVSeriesViewHolder> {

    private List<TVSeries> tvseries;
    private Context context;

    public TVSeriesSearchAdapter() {
        this.tvseries = new ArrayList<>();
    }

    public TVSeriesSearchAdapter(List<TVSeries> tvSeries) {
        this.tvseries = tvSeries;
    }

    @NonNull
    @Override
    public TVSeriesSearchAdapter.TVSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favorite, parent, false);
        return new TVSeriesSearchAdapter.TVSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVSeriesSearchAdapter.TVSeriesViewHolder holder, int position) {
        TVSeries tvSeries = tvseries.get(position);

        holder.titleTxt.setText(tvSeries.getName());
        holder.favTxt.setText(String.valueOf(tvSeries.getVoteAverage()));
        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + tvSeries.getPosterPath())
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), TVSeriesDetailActivity.class);
            intent.putExtra("id", tvSeries.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tvseries.size();
    }

    public void updateTVSeries(List<TVSeries> newTVSeries) {
        this.tvseries = newTVSeries;
        notifyDataSetChanged();
    }

    static class TVSeriesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, favTxt;
        ImageView pic;

        public TVSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            favTxt = itemView.findViewById(R.id.favTxt);
            pic = itemView.findViewById(R.id.imageView7);
        }
    }
}

