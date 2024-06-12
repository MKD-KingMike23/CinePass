package com.miguelrosa.cinepass.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.miguelrosa.cinepass.Domain.Models.Cast;
import com.miguelrosa.cinepass.R;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private List<Cast> castList;
    private Context context;

    public CastAdapter(List<Cast> castList, Context context) {
        this.castList = castList;
        this.context = context;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        if(castList.get(position).getProfilePath()!=null & castList.get(position).getName()!=null & castList.get(position).getCharacter()!=null){
            Cast cast = castList.get(position);
            holder.actorName.setText(cast.getName());
            holder.characterName.setText(cast.getCharacter());

            RequestOptions requestOptions = new RequestOptions().centerCrop();
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + cast.getProfilePath())
                    .apply(requestOptions)
                    .into(holder.actorImage);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView actorImage;
        TextView actorName, characterName;

        CastViewHolder(@NonNull View itemView) {
            super(itemView);
            actorImage = itemView.findViewById(R.id.imageViewCast);
            actorName = itemView.findViewById(R.id.castNametv);
            characterName = itemView.findViewById(R.id.characterNametv);
        }
    }
}
