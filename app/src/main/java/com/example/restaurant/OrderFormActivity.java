package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OrderFormActivity extends AppCompatActivity {

    private EditText phoneEditText, addressEditText, deliveryTimeEditText;
    private TextView emailTextView, totalPriceView;
    private Button confirmOrderButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        // Initialisation des vues
        emailTextView = findViewById(R.id.emailTextView);
        totalPriceView = findViewById(R.id.totalPriceView);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        deliveryTimeEditText = findViewById(R.id.deliveryTimeEditText);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);
        backButton = findViewById(R.id.backButton);

        // Récupérer l'email et le prix total de l'intent
        String userEmail = getIntent().getStringExtra("userEmail");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        // Si l'email n'a pas été transmis correctement, le récupérer depuis Firebase Authentication
        if (userEmail == null || userEmail.isEmpty()) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                userEmail = user.getEmail();
            } else {
                Toast.makeText(this, "Impossible de récupérer l'email. Veuillez vous reconnecter.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // Afficher les données récupérées
        emailTextView.setText("Email : " + userEmail);
        totalPriceView.setText("Prix total : " + totalPrice + " TND");

        // Gestion du bouton de confirmation de commande
        confirmOrderButton.setOnClickListener(v -> {
            String phone = phoneEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String deliveryTime = deliveryTimeEditText.getText().toString().trim();

            if (phone.isEmpty() || address.isEmpty() || deliveryTime.isEmpty()) {
                Toast.makeText(OrderFormActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidTimeFormat(deliveryTime)) {
                Toast.makeText(OrderFormActivity.this, "Format d'heure invalide. Utilisez HH:mm.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simuler la confirmation de la commande
            Toast.makeText(OrderFormActivity.this, "Commande confirmée ! Heure de livraison : " + deliveryTime, Toast.LENGTH_SHORT).show();

            // Ici, vous pouvez ajouter un enregistrement dans Firebase Firestore ou une autre base de données
        });

        // Gestion du bouton retour
        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(OrderFormActivity.this, Menu_Page.class);
            startActivity(intent);
            finish(); // Optionnel : terminer l'activité actuelle pour éviter de revenir en arrière
        });
    }


    private boolean isValidTimeFormat(String time) {
        // Vérifie si l'heure saisie est au format "HH:mm"
        String regex = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";
        return time.matches(regex);
    }
}
