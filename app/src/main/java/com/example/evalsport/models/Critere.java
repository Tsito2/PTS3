package com.example.evalsport.models;

public class Critere {

    private double note;
    private String description;
    private double noteEleve;

    public Critere(String description, double note, double noteEleve){
        this.description = description;
        this.note = note;
        this.noteEleve = noteEleve;
    }

    public double getNote() {
        return note;
    }

    public double getNoteEleve(){
        return noteEleve;
    }

    public String getDescription() {
        return capitalize(description) + " [ " + getFormatedNote(noteEleve) + " / " + getFormatedNote(note) +" ]";
    }

    public Boolean isChecked() {
        return note == noteEleve;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getFormatedNote(double aDouble){
        String noteString = ""+aDouble;
        if (aDouble == (int) aDouble) {
            noteString = String.format("%d",(int)aDouble);
        } else {
            noteString = String.format("%s",aDouble);
        }
        return noteString;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNoteEleve(double noteEleve) {
        this.noteEleve = noteEleve;
    }
}
