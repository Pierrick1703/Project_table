package com.example.Project_Table.Modele;

import java.util.ArrayList;
import java.util.List;

public class Colonne {
    private String Nom;
    private List<Ligne> Ligne;
    private String Type;


    //region Constructeur
    public Colonne(String nom){
        this.Nom = nom;
        this.Type = "String";
        this.Ligne = new ArrayList<Ligne>();
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
    public List<Ligne> getLigne(){
        return this.Ligne;
    }

    public void setNom(String nom){
        this.Nom = nom;
    }
    public void setType(String type){
        this.Type = type;
    }
    public void setLigne(List<Ligne> ligne){
        this.Ligne = ligne;
    }
    //endregion

    public int getLengthLigne(){
        return this.Ligne.size();
    }

}
