package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;
    private List<Categorie> categorieList;
    private DatabaseReference categoriesRef;
    private Button  menuButton, contactButton, avisButton, btnCategories;
    private ImageButton logoutButton;
    private TextView userNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_page);

        // Initialisation des boutons
        recyclerView = findViewById(R.id.recyclerView);
        logoutButton = findViewById(R.id.logoutButton);
        menuButton = findViewById(R.id.btnMenus);
        contactButton = findViewById(R.id.btnContacts);
        avisButton = findViewById(R.id.btnAvis);
        btnCategories = findViewById(R.id.btnCategories);
        userNameText =findViewById(R.id.userNameText);

        // Initialisation du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categorieList = new ArrayList<>();
        adapter = new CategoriesAdapter(this, categorieList);
        recyclerView.setAdapter(adapter);

        // Référence à Firebase
        categoriesRef = FirebaseDatabase.getInstance().getReference("catégories");

        // Récupérer les données depuis Firebase
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categorieList.clear(); // Réinitialiser la liste
                // Parcours de toutes les catégories
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Categorie categorie = snapshot.getValue(Categorie.class);
                    categorieList.add(categorie);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("CategoriePage", "Erreur de récupération des catégories", databaseError.toException());
            }
        });

        // Déconnexion
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(CategoriePage.this, Login.class); // Activité de connexion
            startActivity(intent);
            finish();
        });

        // Afficher le nom de l'utilisateur dans la barre de navigation
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null && !email.isEmpty()) {
                userNameText.setText("Bienvenue, " + email);
            } else {
                userNameText.setText("Bienvenue, utilisateur");
            }
        } else {
            userNameText.setText("Utilisateur non connecté");
        }

        // Lien vers la page Menu
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriePage.this, Menu_Page.class); // Remplacez par la page des menus
            startActivity(intent);
        });

        // Lien vers la page Avis
        avisButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriePage.this, Avis_Page.class); // Remplacez par la page des avis
            startActivity(intent);
        });

        // Lien vers la page Contact
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriePage.this, Contact_Page.class); // Remplacez par la page de contact
            startActivity(intent);
        });

        // Lien vers la page Categorie (actualiser la même page)
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriePage.this, CategoriePage.class); // Actualiser la page de catégories
            startActivity(intent);
        });
    }
}
