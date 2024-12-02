package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Localisation_Page extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Localisation_Page";
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_localisation_page);

        // Charger le fragment de la carte
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Configurer le callback
        } else {
            Log.e(TAG, "Fragment de la carte non trouvé !");
        }

        // Initialiser le bouton de retour
        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers la page de menu
                Intent intent = new Intent(Localisation_Page.this, Acceuilpage.class);
                startActivity(intent);
                finish(); // Facultatif, pour fermer la page de localisation
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Carte prête !");
        gMap = googleMap;

        // Coordonnées du centre de Nabeul
        LatLng restaurantLocation = new LatLng(36.451174, 10.737692); // Nabeul Centre
        gMap.addMarker(new MarkerOptions()
                .position(restaurantLocation)
                .title("Mon Restaurant")
                .snippet("Adresse : Nabeul Centre, Tunisie"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15f)); // Zoom adapté
    }
}
