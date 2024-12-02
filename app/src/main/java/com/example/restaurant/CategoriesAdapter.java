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
import com.squareup.picasso.Callback;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategorieViewHolder> {

    private List<Categorie> categorieList;
    private Context context;

    public CategoriesAdapter(Context context, List<Categorie> categorieList) {
        this.context = context;
        this.categorieList = categorieList;
    }

    @NonNull
    @Override
    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gonfler le layout de chaque élément de catégorie
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        Categorie categorie = categorieList.get(position);
        holder.categoryName.setText(categorie.getName());

        // Charger l'image depuis l'URL de Firebase
        String imageUrl = categorie.getImageUrl();
        Picasso.get()
                .load(imageUrl)  // Charger l'image depuis l'URL
                .into(holder.categoryImage);  // Afficher l'image dans l'ImageView
    }

    @Override
    public int getItemCount() {
        return categorieList.size();
    }

    public static class CategorieViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public CategorieViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}