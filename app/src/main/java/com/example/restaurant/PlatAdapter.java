package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlatAdapter extends RecyclerView.Adapter<PlatAdapter.PlatViewHolder> {
    private List<Plat> platList;
    private Context context;

    // Constructeur
    public PlatAdapter(List<Plat> platList, Context context) {
        this.platList = platList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Créer la vue pour chaque élément de la RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_plat, parent, false);
        return new PlatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatViewHolder holder, int position) {
        // Récupérer les données du plat actuel
        Plat plat = platList.get(position);

        // Remplir les vues avec les données du plat
        holder.platName.setText(plat.getName());

        // Charger l'image du plat avec Glide
        Glide.with(context)
                .load(plat.getImageUrl())
                .into(holder.platImage);

        // Ajouter un clic sur le bouton "Voir Détail"
        holder.btnDetail.setOnClickListener(v -> {
            // Créer une intention pour passer à l'activité de détail
            Intent intent = new Intent(context, DetailPlatActivity.class);
            intent.putExtra("platName", plat.getName());
            intent.putExtra("platDescription", plat.getDescription());
            intent.putExtra("platPrice", plat.getPrice());
            intent.putExtra("platImageUrl", plat.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Retourner le nombre d'éléments dans la liste
        return platList.size();
    }

    // Méthode pour mettre à jour les données de la RecyclerView
    public void updateData(List<Plat> filteredList) {
        this.platList = filteredList;
        notifyDataSetChanged(); // Notifier que les données ont changé
    }

    // Classe interne pour le ViewHolder
    public static class PlatViewHolder extends RecyclerView.ViewHolder {
        ImageView platImage;
        TextView platName;
        Button btnDetail;

        public PlatViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialiser les vues
            platImage = itemView.findViewById(R.id.platImage);
            platName = itemView.findViewById(R.id.platName);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
