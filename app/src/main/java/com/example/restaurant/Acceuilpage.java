package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

public class Acceuilpage extends AppCompatActivity {
    private TextView nameText, descriptionText, addressText, typeText, phoneText, userNameText;
    private ImageView restaurantImage;
    private Button  localisationButton, menuButton, contactButton, avisButton,btnCategories;
    private ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuilpage);

        // Initialiser les vues
        nameText = findViewById(R.id.nameText);
        descriptionText = findViewById(R.id.descriptionText);
        addressText = findViewById(R.id.addressText);
        typeText = findViewById(R.id.typeText);
        phoneText = findViewById(R.id.phoneText);
        restaurantImage = findViewById(R.id.restaurantImage);
        userNameText = findViewById(R.id.userNameText);
        logoutButton = findViewById(R.id.logoutButton);
        localisationButton = findViewById(R.id.localisationButton);  // Initialisation du bouton
        menuButton = findViewById(R.id.btnMenus);
        contactButton = findViewById(R.id.btnContacts);
        avisButton = findViewById(R.id.btnAvis);
        btnCategories = findViewById(R.id.btnCategories);

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

        // Lire les données de Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("restaurant");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        // Remplir les champs avec les données récupérées avec des phrases descriptives
                        nameText.setText("Nom du restaurant : " + (restaurant.getNomR() != null && !restaurant.getNomR().isEmpty() ? restaurant.getNomR() : "Nom non défini"));
                        descriptionText.setText("À propos de nous : " + (restaurant.getDescription() != null && !restaurant.getDescription().isEmpty() ? restaurant.getDescription() : "Description non définie"));
                        addressText.setText("Adresse : " + (restaurant.getAddress() != null && !restaurant.getAddress().isEmpty() ? restaurant.getAddress() : "Adresse non définie"));
                        typeText.setText("Type de notre cuisine : " + (restaurant.getType() != null && !restaurant.getType().isEmpty() ? restaurant.getType() : "Type non défini"));
                        phoneText.setText("Contactez-nous sur ce numéro : " + (restaurant.getNumero() != null ? String.valueOf(restaurant.getNumero()) : "Numéro non défini"));

                        // Charger l'image depuis l'URL avec Picasso
                        String imageUrl = restaurant.getUrli();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(restaurantImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // L'image a été chargée avec succès
                                }

                                @Override
                                public void onError(Exception e) {
                                    restaurantImage.setImageDrawable(null);  // Ne pas afficher d'image par défaut
                                }
                            });
                        } else {
                            restaurantImage.setImageDrawable(null);
                        }
                    } else {
                        setDefaultValues();
                    }
                } else {
                    setDefaultValues();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setDefaultValues();
            }
        });

        // Déconnexion
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Acceuilpage.this, Login.class); // Activité de connexion
            startActivity(intent);
            finish();
        });

        // Lien vers la page de localisation
        localisationButton.setOnClickListener(v -> {
            Intent intent = new Intent(Acceuilpage.this, Localisation_Page.class); // Page de localisation
            startActivity(intent);
        });

        // Lien vers la page Menu
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(Acceuilpage.this, Menu_Page.class); // Remplacez par la page des menus
            startActivity(intent);
        });

        // Lien vers la page Avis
        avisButton.setOnClickListener(v -> {
            Intent intent = new Intent(Acceuilpage.this, Avis_Page.class); // Remplacez par la page des avis
            startActivity(intent);
        });

        // Lien vers la page Contact
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(Acceuilpage.this, Contact_Page.class); // Remplacez par la page de contact
            startActivity(intent);
        });
        // Lien vers la page Contact
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(Acceuilpage.this, CategoriePage.class); // Remplacez par la page de contact
            startActivity(intent);
        });
    }

    private void setDefaultValues() {
        nameText.setText("Nom du restaurant : Non défini");
        descriptionText.setText("À propos de nous : Non défini");
        addressText.setText("Adresse : Non définie");
        typeText.setText("Type de notre cuisine : Non défini");
        phoneText.setText("Contactez-nous sur ce numéro : 27371075");
        restaurantImage.setImageDrawable(null);
    }
}