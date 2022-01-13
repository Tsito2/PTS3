package com.example.evalsport.models;

import com.example.evalsport.R;

public class Sport {

    private int img;
    private String nom;

    public Sport(String nom){
        this.nom = nom;
        switch (nom){
            case "Escalade":
                img = R.drawable.escalade;
                break;
            case "Basketball":
                img = R.drawable.basket;
                break;
            case "Football":
                img = R.drawable.football;
                break;
            case "Natation":
                img = R.drawable.natation;
                break;
            case "110m haies":
                img = R.drawable.course_haie;
                break;
            case "Saut en hauteur":
                img = R.drawable.saut;
                break;
                default:
                    img = R.drawable.sport;
                    break;
        }
    }

    public int getImg() {
        return img;
    }

    public String getNom() {
        return nom;
    }
}
