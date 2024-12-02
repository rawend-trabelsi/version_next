package com.example.restaurant;

public class Restaurant {
    private String NomR;
    private String Description;
    private String Address;
    private String Type;
    private Integer Numero; // Changement de int à Integer pour permettre les valeurs nulles
    private String urli;

    // Constructeur par défaut (pour Firebase)
    public Restaurant() {}

    // Getters
    public String getNomR() {
        return NomR;
    }

    public String getDescription() {
        return Description;
    }

    public String getAddress() {
        return Address;
    }

    public String getType() {
        return Type;
    }

    public Integer getNumero() { // Retourne un Integer pour gérer les numéros formatés
        return Numero;
    }

    public String getUrli() {
        return urli;
    }

    // Setters
    public void setNomR(String nomR) {
        this.NomR = nomR;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public void setNumero(Integer numero) { // Accepte un Integer pour la flexibilité
        this.Numero = numero;
    }

    public void setUrli(String urli) {
        this.urli = urli;
    }
}
