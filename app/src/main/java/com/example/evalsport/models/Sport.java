package com.example.evalsport.models;

public class Sport {

    private int img;
    private String nom;

    public Sport(String nom, int img){
        this.nom = nom;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public String getNom() {
        return nom;
    }
}
