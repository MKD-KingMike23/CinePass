package com.miguelrosa.cinepass.Adapters;

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

import java.util.List;

public class TVSeriesListAdapter extends RecyclerView.Adapter<TVSeriesListAdapter.ViewHolder>{
    List<TVSeries> items;
    Context context;

    public TVSeriesListAdapter(List<TVSeries> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TVSeriesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favorite,parent,false);
        return new TVSeriesListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TVSeriesListAdapter.ViewHolder holder, int position) {
        TVSeries tvSeries = items.get(position);

        holder.titleTxt.setText(tvSeries.getName());
        holder.favTxt.setText(String.valueOf(tvSeries.getVoteAverage()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

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
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt, favTxt;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            favTxt = itemView.findViewById(R.id.favTxt);
            pic = itemView.findViewById(R.id.imageView7);
        }
    }
}