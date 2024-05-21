package com.miguelrosa.cinepass.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.miguelrosa.cinepass.Activities.DetailActivity;
import com.miguelrosa.cinepass.Activities.MainActivity;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Movie;
import com.miguelrosa.cinepass.Domain.PopularMoviesResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ViewholderFilmBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {
    List<Movie> items;
    Context context;

    public FilmListAdapter(List<Movie> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_film,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListAdapter.ViewHolder holder, int position) {
        Movie movie = items.get(position);

        holder.titleTxt.setText(movie.getTitle());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

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
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.imageView7);
        }
    }
}
