package com.example.evalsport.models;

public class Student {
    private String nom;
    private String prenom;
    private String classe;

    public Student(String nom, String prenom, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getClasse() {
        return classe;
    }
}
