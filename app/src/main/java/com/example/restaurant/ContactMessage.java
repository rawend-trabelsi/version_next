package com.example.restaurant;

public class ContactMessage {
    private String email;
    private String message;
    private String timestamp;
    // Constructeur vide requis pour la désérialisation de Firebase
public ContactMessage() {     }
// Constructeur avec paramètres
public ContactMessage(String email, String message, String timestamp) {
    this.email = email;
    this.message = message;
    this.timestamp = timestamp;
}     public String getEmail() {
    return email;     }
    public void setEmail(String email) {
    this.email = email;     }
    public String getMessage() {
    return message;     }
    public void setMessage(String message) {
    this.message = message;     }
    public String getTimestamp() {
    return timestamp;     }
    public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
} }