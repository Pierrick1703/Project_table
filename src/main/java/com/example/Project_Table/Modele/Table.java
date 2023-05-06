package com.example.Project_Table.Modele;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Table {
    private List<Colonne> Colonne;
    private String Nom;

    //region Constructeur
    public Table(String nom){
        this.Colonne = Initialisation();
        this.Nom = nom;
    }
    public Table(JSONObject json){
        this.Nom = json.getString("Nom");
        this.Colonne = new ArrayList<Colonne>();
        JSONArray colonneJson = json.getJSONArray("Colonne");
        for(int i = 0;i < colonneJson.length();i++){
            this.Colonne.add(new Colonne((colonneJson.getJSONObject(i))));
        }
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
            try{
                if(result < colonne.getLengthLigne())
                    result = colonne.getLengthLigne();
            }catch (Exception e){

            }
        }
        return result;
    }
    public List<Colonne> Initialisation(){
        List<Colonne> result = new ArrayList<Colonne>();
        Colonne colonne = new Colonne("Numéro");
        List<Ligne> listLigne = new ArrayList<Ligne>();
        Ligne l = new Ligne("1",1,"");
        listLigne.add(l);
        colonne.setLigne(listLigne);
        result.add(colonne);
        return result;
    }
    public void addColonne(Colonne uneColonne){
        this.Colonne.add(uneColonne);
        verificationNombreLigne();
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

    public String getFormulaCell(String colonneName, int ligneNumber){
        String result = "";
        for(int i=0;i<Colonne.size();i++){
            if(Colonne.get(i).getNom() == colonneName){
                Colonne uneColonne = Colonne.get(i);
                Ligne uneLigne = uneColonne.getLigne().get(ligneNumber);
                result = uneLigne.getForumle();
            }
        }
        return result;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("Nom", this.Nom);

        JSONArray colonneJson = new JSONArray();
        for (Colonne currentColonne : this.Colonne) {
            colonneJson.put(currentColonne.toJson());
        }
        json.put("Colonne", colonneJson);

        return json;
    }

    public boolean verificationNomColonne(String nomColonne){
        boolean result = true;
        for(Colonne currentColonne : this.Colonne){
            if(Objects.equals(nomColonne,currentColonne.getNom())){
                result = false;
            }
        }
        return result;
    }

    public void verificationNombreLigne(){
        List<Ligne> listLigne = new ArrayList<Ligne>();
        for(Colonne currentColonne : this.Colonne){
            if(currentColonne.getNom() == "Numéro"){
                for(int i = 0;i<this.getLongestLigne();i++){
                    Ligne l = new Ligne(i+"",i,"");
                    listLigne.add(l);
                }
                currentColonne.setLigne(listLigne);
            }
        }
    }
    public Colonne getColonne(int indexColonne){
        Colonne resultColonne = null;
        resultColonne = this.Colonne.get(indexColonne);
        return resultColonne;
    }
    public void changeValueLigne(int indexLigne,int indexColonne,String oldValue,String newValue){
        Ligne row = Colonne.get(indexColonne).getLigne(indexLigne);
        if(row.getValeur() == oldValue){
            row.setValeur(newValue);
        }
    }
}
