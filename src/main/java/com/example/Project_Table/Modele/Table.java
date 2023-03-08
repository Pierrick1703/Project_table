package com.example.Project_Table.Modele;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Table {
    private List<Colonne> Colonne;
    private String Nom;

    //region Constructeur
    public Table(String nom){
        this.Nom = nom;
    }
    //endregion
    //region Getter and Setter
    public String getNom(){
        return this.Nom;
    }
    public List<Colonne> getColonne(){
        return this.Colonne;
    }
    public void setColonne(List<Colonne> colonne){
        this.Colonne = colonne;
    }

    public void setNom(String nom){
        this.Nom = nom;
    }
    //endregion

}
