package com.example.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlatCategoryAdapter extends RecyclerView.Adapter<PlatCategoryAdapter.PlatViewHolder> {

    private List<Plat> platList;
    private Context context;

    public PlatCategoryAdapter(Context context, List<Plat> platList) {
        this.context = context;
        this.platList = platList;
    }

    @NonNull
    @Override
    public PlatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gonfler le layout pour chaque plat
        View view = LayoutInflater.from(context).inflate(R.layout.item_plat, parent, false);
        return new PlatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatViewHolder holder, int position) {
        Plat plat = platList.get(position);
        holder.platName.setText(plat.getName());

        // Charger l'image du plat
        Picasso.get().load(plat.getImageUrl()).into(holder.platImage);
    }

    @Override
    public int getItemCount() {
        return platList.size();
    }

    public static class PlatViewHolder extends RecyclerView.ViewHolder {
        TextView platName;
        ImageView platImage;

        public PlatViewHolder(View itemView) {
            super(itemView);
            platName = itemView.findViewById(R.id.platName);
            platImage = itemView.findViewById(R.id.platImage);
        }
    }
}
