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
import com.miguelrosa.cinepass.Activities.DetailActivity;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.R;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MovieSearchAdapter() {
        this.movies = new ArrayList<>();
    }

    public MovieSearchAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favorite, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.titleTxt.setText(movie.getTitle());
        holder.favTxt.setText(String.valueOf(movie.getVoteAverage()));
        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", movie.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, favTxt;
        ImageView pic;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            favTxt = itemView.findViewById(R.id.favTxt);
            pic = itemView.findViewById(R.id.imageView7);
        }
    }
}
