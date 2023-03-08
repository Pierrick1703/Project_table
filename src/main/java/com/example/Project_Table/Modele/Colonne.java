package com.example.Project_Table.Modele;

import java.util.List;

public class Colonne {
    private String Nom;
    private List<String> Ligne;
    private String Type;

    //region Constructeur
    public Colonne(String nom){
        this.Nom = nom;
        this.Type = "String";
    }
    public Colonne(String nom,String type){
        this.Nom = nom;
        this.Type = type;
    }
    //endregion
    //region Getter and setter
    public String getNom(){
        return this.Nom;
    }
    public String getType(){
        return this.Type;
    }
    public List<String> getLigne(){
        return this.Ligne;
    }
    public void setNom(String nom){
        this.Nom = nom;
    }
    public void setType(String type){
        this.Type = type;
    }
    public void setLigne(List<String> ligne){
        this.Ligne = ligne;
    }
    //endregion


}