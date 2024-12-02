package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText usernameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation de FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Déclaration et liaison des composants
        usernameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input_sign_up);
        passwordInput = findViewById(R.id.password_input_sign_up);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_view_login);

        textView.setOnClickListener(view -> {
            // Créer un Intent pour démarrer LoginActivity
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            // Facultatif : Fermer l'activité actuelle
            finish();
        });

        // Définir une action pour le bouton "Register"
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            // Afficher la ProgressBar
            progressBar.setVisibility(View.VISIBLE);

            // Validation des champs
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Enregistrer l'utilisateur
                registerUser(email, password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Vérifier si l'utilisateur est déjà connecté (non nul) et rediriger si nécessaire
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Si l'utilisateur est déjà connecté, rediriger vers l'activité principale (ou une autre)
            Intent intent = new Intent(Register.this, Register.class);  // Remplacez MainActivity par l'activité appropriée
            startActivity(intent);
            finish(); // Fermer l'activité d'enregistrement
        }
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                        // Rediriger vers la page de connexion après l'inscription
                        Intent intent = new Intent(Register.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Pour s'assurer que la page de connexion est la seule activité active
                        startActivity(intent);
                        finish(); // Fermer l'activité d'enregistrement
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "Welcome, " + user.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User is null");
        }
    }
}