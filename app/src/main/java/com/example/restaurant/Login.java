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

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signUpLink;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Initialiser les composants de l'interface utilisateur
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signUpLink = findViewById(R.id.sign_up_link);
        progressBar = findViewById(R.id.progress_bar);

        // Définir une action pour le bouton "Login"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs des champs
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Vérifier si les champs ne sont pas vides
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Afficher la barre de progression
                progressBar.setVisibility(View.VISIBLE);

                // Tenter de se connecter avec FirebaseAuth
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, task -> {
                            // Cacher la barre de progression
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                // Connexion réussie
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // Échec de la connexion
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });
            }
        });


        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers RegisterActivity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    // Supprimer la méthode onStart() pour éviter la redirection automatique
    // @Override
    // protected void onStart() {
    //     super.onStart();
    //     FirebaseUser currentUser = auth.getCurrentUser();
    //     if (currentUser != null) {
    //         Intent intent = new Intent(Login.this, Acceuilpage.class);
    //         startActivity(intent);
    //         finish(); // Fermer l'activité de connexion
    //     }
    // }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // L'utilisateur est connecté, rediriger vers Acceuilpage
            Intent intent = new Intent(Login.this, Acceuilpage.class);
            startActivity(intent);
            finish(); // Fermer l'activité de connexion
        } else {
            // Si l'utilisateur est null, afficher un message d'erreur
            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
