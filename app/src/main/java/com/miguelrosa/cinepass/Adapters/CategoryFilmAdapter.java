package com.miguelrosa.cinepass.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelrosa.cinepass.R;

import java.util.ArrayList;

public class CategoryFilmAdapter extends RecyclerView.Adapter<CategoryFilmAdapter.ViewHolder> {
    ArrayList<GenresItem> items;
    Context context;

    @NonNull
    @Override
    public CategoryFilmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryFilmAdapter.ViewHolder holder, int position) {
        holder.
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
