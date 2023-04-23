package com.example.Project_Table.Modele;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ligne {

    private String Valeur;

    private Integer Numero;

    private String Forumle;

    public Ligne(String valeur, Integer numero, String formule){
        this.Valeur = valeur;
        this.Numero = numero;
        this.Forumle = formule;
    }

    public Ligne(JSONObject json){
        this.Valeur = json.getString("Valeur");
        this.Numero = json.getInt("Numero");
        this.Forumle = json.getString("Formule");
    }

    public String getValeur() {
        return Valeur;
    }
    public Integer getNumero() {
        return Numero;
    }
    public String getForumle() {
        return Forumle;
    }

    public void setForumle(String forumle) {
        Forumle = forumle;
    }

    public void setNumero(Integer numero) {
        Numero = numero;
    }

    public void setValeur(String valeur) {
        Valeur = valeur;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("Valeur", this.Valeur);
        json.put("Numero", this.Numero);
        json.put("Formule", this.Forumle);
        return json;
    }
}
