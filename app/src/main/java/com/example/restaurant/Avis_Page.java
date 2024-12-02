package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Avis_Page extends AppCompatActivity {
    private Button localisationButton, menuButton, contactButton, avisButton, btnCategories;
    private ImageButton logoutButton;
    private TextView userNameText;
    private Spinner dishSpinner;
    private RatingBar ratingBar;
    private EditText commentInput;
    private Button submitReviewButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avis_page);

        // Initialisation des vues
        logoutButton = findViewById(R.id.logoutButton);
        localisationButton = findViewById(R.id.localisationButton);
        menuButton = findViewById(R.id.btnMenus);
        contactButton = findViewById(R.id.btnContacts);
        avisButton = findViewById(R.id.btnAvis);
        btnCategories = findViewById(R.id.btnCategories);
        userNameText = findViewById(R.id.userNameText); // Assurez-vous que cette ID existe dans votre layout
        dishSpinner = findViewById(R.id.dishSpinner);
        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Initialisation de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");

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

        // Gestion des actions des boutons
        setupButtonListeners();

        // Gestion de la soumission de l'avis
        submitReviewButton.setOnClickListener(v -> submitReview());
    }

    private void setupButtonListeners() {
        // Déconnexion
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Avis_Page.this, Login.class); // Activité de connexion
            startActivity(intent);
            finish();
        });

        // Lien vers la page Menu
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(Avis_Page.this, Menu_Page.class);
            startActivity(intent);
        });

        // Lien vers la page Avis
        avisButton.setOnClickListener(v -> {
            Intent intent = new Intent(Avis_Page.this, Avis_Page.class);
            startActivity(intent);
        });

        // Lien vers la page Contact
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(Avis_Page.this, Contact_Page.class);
            startActivity(intent);
        });

        // Lien vers la page Catégories
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(Avis_Page.this, CategoriePage.class);
            startActivity(intent);
        });
    }

    private void submitReview() {
        String selectedDish = dishSpinner.getSelectedItem().toString();
        float rating = ratingBar.getRating();
        String comment = commentInput.getText().toString();

        if (selectedDish.isEmpty() || rating == 0 || comment.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtenir l'utilisateur actuel
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vous devez être connecté pour laisser un avis", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String reviewId = databaseReference.push().getKey();

        if (reviewId == null) {
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'avis", Toast.LENGTH_SHORT).show();
            return;
        }

        // Préparer les données de l'avis
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("userId", userId);
        reviewData.put("dish", selectedDish);
        reviewData.put("rating", rating);
        reviewData.put("comment", comment);

        // Enregistrer l'avis dans Firebase
        databaseReference.child(reviewId).setValue(reviewData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Merci pour votre avis!", Toast.LENGTH_SHORT).show();

                // Réinitialiser les champs
                ratingBar.setRating(0);
                commentInput.setText("");
            } else {
                Toast.makeText(this, "Erreur lors de l'enregistrement de l'avis", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
