package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailPlatActivity extends AppCompatActivity {

    private ImageView platImageView;
    private TextView platNameView, platDescriptionView, platPriceView, totalPriceView;
    private EditText quantityEditText;
    private Button orderButton;
    private ImageButton backButton; // ImageButton pour le retour

    private double platPrice; // Prix unitaire du plat
    private String userEmail; // Email de l'utilisateur connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plat);

        // Initialisation des vues
        platImageView = findViewById(R.id.platImageView);
        platNameView = findViewById(R.id.platNameView);
        platDescriptionView = findViewById(R.id.platDescriptionView);
        platPriceView = findViewById(R.id.platPriceView);
        totalPriceView = findViewById(R.id.totalPriceView);
        quantityEditText = findViewById(R.id.quantityEditText);
        orderButton = findViewById(R.id.orderButton);
        backButton = findViewById(R.id.backButton); // Initialisation du bouton retour

        // Récupérer les données du plat
        Intent intent = getIntent();
        String name = intent.getStringExtra("platName");
        String description = intent.getStringExtra("platDescription");
        platPrice = intent.getDoubleExtra("platPrice", 0);
        String imageUrl = intent.getStringExtra("platImageUrl");

        // Récupérer l'email de l'utilisateur connecté via Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail(); // Récupérer l'email de l'utilisateur connecté
        } else {
            Toast.makeText(this, "Veuillez vous connecter pour continuer.", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si l'utilisateur n'est pas connecté
            return;
        }

        // Afficher les détails du plat
        platNameView.setText(name);
        platDescriptionView.setText(description);
        platPriceView.setText("Prix unitaire : " + platPrice + " TND");
        Glide.with(this).load(imageUrl).into(platImageView);

        // Mettre à jour le prix total en fonction de la quantité saisie
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Gestion du bouton "Commander"
        orderButton.setOnClickListener(v -> {
            String quantityStr = quantityEditText.getText().toString().trim();
            if (quantityStr.isEmpty() || Integer.parseInt(quantityStr) <= 0) {
                Toast.makeText(DetailPlatActivity.this, "Veuillez entrer une quantité valide.", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double totalPrice = quantity * platPrice;

            // Rediriger vers la page de formulaire de commande
            Intent orderIntent = new Intent(DetailPlatActivity.this, OrderFormActivity.class);
            orderIntent.putExtra("platName", name);
            orderIntent.putExtra("quantity", quantity);
            orderIntent.putExtra("totalPrice", totalPrice);
            orderIntent.putExtra("userEmail", userEmail); // Transmettez l'email récupéré
            startActivity(orderIntent);
        });

        // Gestion du bouton retour
        backButton.setOnClickListener(v -> {
            // Retour à l'activité MenuPage
            Intent intentBack = new Intent(DetailPlatActivity.this, Menu_Page.class);
            startActivity(intentBack);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); // Animation de transition
            finish(); // Facultatif
        });
    }

    // Met à jour le prix total
    private void updateTotalPrice() {
        String quantityStr = quantityEditText.getText().toString().trim();
        if (!quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);
            double totalPrice = quantity * platPrice;
            totalPriceView.setText("Prix total : " + totalPrice + " TND");
        } else {
            totalPriceView.setText("Prix total : 0 TND");
        }
    }
}
