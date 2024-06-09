package com.miguelrosa.cinepass.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miguelrosa.cinepass.Domain.Responses.WatchProviderResponse;
import com.miguelrosa.cinepass.R;

import java.util.List;

public class WatchProviderAdapter extends RecyclerView.Adapter<WatchProviderAdapter.ProviderViewHolder> {

    private List<WatchProviderResponse.Provider> providers;

    public WatchProviderAdapter(List<WatchProviderResponse.Provider> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watch_provider, parent, false);
        return new ProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        WatchProviderResponse.Provider provider = providers.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + provider.getLogoPath())
                .into(holder.providerLogo);
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    static class ProviderViewHolder extends RecyclerView.ViewHolder {
        ImageView providerLogo;

        ProviderViewHolder(View itemView) {
            super(itemView);
            providerLogo = itemView.findViewById(R.id.providerLogo);
        }
    }
}
