package com.example.Project_Table.Modele;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public Colonne(JSONObject json){
        this.Nom = json.getString("Nom");
        this.Type = json.getString("Type");
        JSONArray ligneJson = json.getJSONArray("Ligne");
        this.Ligne = new ArrayList<Ligne>();
        for(int i = 0;i < ligneJson.length();i++){
            this.Ligne.add(new Ligne((ligneJson.getJSONObject(i))));
        }
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

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("Nom", this.Nom);
        json.put("Type", this.Type);

        JSONArray lignesJson = new JSONArray();
        for (Ligne currentLigne : this.Ligne) {
            lignesJson.put(currentLigne.toJson());
        }
        json.put("Ligne", lignesJson);

        return json;
    }
    public Ligne getLigne(int indexLigne){
        Ligne resultLigne = null;
        resultLigne = this.Ligne.get(indexLigne);
        return resultLigne;
    }
}
