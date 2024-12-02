package com.example.restaurant;

public class Plat {
    private String name;
    private String categoryId;
    private String description;
    private double price;
    private String imageUrl;

    public Plat() {} // Constructeur vide pour Firebase

    public Plat(String name, String categoryId, String description, double price, String imageUrl) {
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
