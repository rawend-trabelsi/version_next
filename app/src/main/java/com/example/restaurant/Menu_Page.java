package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Menu_Page extends AppCompatActivity {
    private RecyclerView platRecyclerView; // RecyclerView pour afficher les plats
    private EditText searchBar;           // Barre de recherche
    private PlatAdapter platAdapter;      // Adaptateur pour la RecyclerView
    private List<Plat> platList = new ArrayList<>(); // Liste des plats
    private Button menuButton, avisButton, btnCategories, contactButton;
    private TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plat); // Assurez-vous que "plat.xml" est le bon fichier de mise en page

        // Initialisation des composants
        platRecyclerView = findViewById(R.id.platRecyclerView);
        searchBar = findViewById(R.id.search_bar);

        // Initialisation des boutons
        menuButton = findViewById(R.id.btnMenus);
        avisButton = findViewById(R.id.btnAvis);
        contactButton = findViewById(R.id.btnContacts);
        btnCategories = findViewById(R.id.btnCategories);
        userNameText = findViewById(R.id.userNameText);

        // Configuration de la RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 pour deux colonnes
        platRecyclerView.setLayoutManager(gridLayoutManager);

        // Charger les plats depuis Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("plats");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                platList.clear(); // Réinitialiser la liste avant de la remplir
                for (DataSnapshot platSnapshot : snapshot.getChildren()) {
                    Plat plat = platSnapshot.getValue(Plat.class);
                    if (plat != null) {
                        platList.add(plat); // Ajouter chaque plat à la liste
                    }
                }
                // Initialiser et attacher l'adaptateur une fois les données chargées
                platAdapter = new PlatAdapter(platList, Menu_Page.this);
                platRecyclerView.setAdapter(platAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Menu_Page.this, "Erreur de chargement des plats : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter un écouteur à la barre de recherche
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString()); // Filtrer les plats lorsque le texte change
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
            Intent intent = new Intent(Menu_Page.this, Menu_Page.class); // Redirection vers la page des menus
            startActivity(intent);
        });

        // Lien vers la page Avis
        avisButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Page.this, Avis_Page.class); // Redirection vers la page des avis
            startActivity(intent);
        });

        // Lien vers la page Contact
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Page.this, Contact_Page.class); // Redirection vers la page de contact
            startActivity(intent);
        });

        // Lien vers la page Catégories
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(Menu_Page.this, CategoriePage.class); // Redirection vers la page des catégories
            startActivity(intent);
        });
    }

    // Méthode pour filtrer les plats en fonction du texte saisi
    private void filter(String text) {
        List<Plat> filteredList = new ArrayList<>();
        for (Plat plat : platList) {
            if (plat.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(plat);
            }
        }
        // Mettre à jour l'adaptateur avec la liste filtrée
        if (platAdapter != null) {
            platAdapter.updateData(filteredList);
        }
    }
}
