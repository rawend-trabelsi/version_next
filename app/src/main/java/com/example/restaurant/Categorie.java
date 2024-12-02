package com.example.restaurant;

public class Categorie {

    private String name;
    private String imageUrl;

    // Constructeur vide pour Firebase
    public Categorie() {}

    // Constructeur avec paramètres
    public Categorie(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
