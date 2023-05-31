package com.example.Project_Table.Modele;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ligne {

    private String Valeur;

    private Integer Numero;

    private String Forumle;

    private List<String> UsedToFormule = new ArrayList<>();

    //region Constructeur
    public Ligne(String valeur, Integer numero, String formule){
        this.Valeur = valeur;
        this.Numero = numero;
        this.Forumle = formule;
    }

    public Ligne(JSONObject json){
        this.Valeur = json.getString("Valeur");
        this.Numero = json.getInt("Numero");
        this.Forumle = json.getString("Formule");
        /*JSONArray usedToFormuleArray = json.getJSONArray("UsedToFormule");
        this.UsedToFormule = new ArrayList<>();
        for (int i = 0; i < usedToFormuleArray.length(); i++) {
            String usedValue = usedToFormuleArray.getString(i);
            this.UsedToFormule.add(usedValue);
        }*/
    }
    //endregion
    //region getter and setter
    public String getValeur() {
        return Valeur;
    }
    public Integer getNumero() {
        return Numero;
    }
    public String getForumle() {
        return Forumle;
    }
    public List<String> getUsedToFormule(){
        return UsedToFormule;
    }

    public void setUsedToFormule(List<String> usedToFormule){
        UsedToFormule = usedToFormule;
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
    //endregion

    public void addUsedToFormule(String value){
        UsedToFormule.add(value);
    }
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("Valeur", this.Valeur);
        json.put("Numero", this.Numero);
        json.put("Formule", this.Forumle);
        JSONArray usedToFormuleJson = new JSONArray();
        for (String usedToFormuleCurrent : this.UsedToFormule) {
            usedToFormuleJson.put(usedToFormuleCurrent);
        }
        json.put("UsedToFormule", usedToFormuleJson);
        return json;
    }


}
