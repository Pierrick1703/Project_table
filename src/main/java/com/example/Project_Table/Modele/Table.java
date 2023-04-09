package com.example.Project_Table.Modele;

import java.util.*;

public class Table {
    private List<Colonne> Colonne;
    private String Nom;

    //region Constructeur
    public Table(String nom){
        this.Colonne = Initialisation();
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
    public int getLongestLigne(){
        int result = 0;
        for(Colonne colonne : this.Colonne){
            if(result < colonne.getLengthLigne())
                result = colonne.getLengthLigne();
        }
        return result;
    }
    public List<Colonne> Initialisation(){
        List<Colonne> result = new ArrayList<Colonne>();
        Colonne colonne = new Colonne("Numéro");
        result.add(colonne);
        return result;
    }
    public void addColonne(Colonne uneColonne){
        this.Colonne.add(uneColonne);
    }
    public String getValueCell(String colonneName, int ligneNumber){
        String result = "";
        for(int i=0;i<Colonne.size();i++){
            if(Colonne.get(i).getNom() == colonneName){
                Colonne uneColonne = Colonne.get(i);
                Ligne uneLigne = uneColonne.getLigne().get(ligneNumber);
                result = uneLigne.getValeur();
            }
        }
        return result;
    }
}
