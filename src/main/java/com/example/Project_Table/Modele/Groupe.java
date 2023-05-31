package com.example.Project_Table.Modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Groupe {

    //region Variable
    private String Nom;
    private ArrayList<Table> table = new ArrayList<>();
    private int Nombre_utilisateur;
    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    Map<String, String> Privillege = new HashMap<>();
    //endregion
    //region Constructeur
    public Groupe(String nom){
        this.Nom = nom;
    }

    public Groupe(String nom, Map<String, String> privillege){
        this.Nom = nom;
        this.Privillege = privillege;
    }

    //endregion
    //region Getter and setter
    public String getNom(){
        return Nom;
    }

    public void setNom(String nom){
        this.Nom = nom;
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public void setSelected(boolean selected) {
        selectedProperty.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public int getNombre_utilisateur() {
        return Nombre_utilisateur;
    }

    public void setNombre_utilisateur(int nombre_utilisateur) {
        Nombre_utilisateur = nombre_utilisateur;
    }

    public ArrayList<Table> getTable() {
        return table;
    }

    public void setTable(ArrayList<Table> table) {
        table = table;
    }

    public Map<String, String> getPrivillege(String s) {
        return Privillege;
    }

    public void setPrivillege(Map<String, String> privillege) {
        Privillege = privillege;
    }

    public String getPrivillegeValue(String value) {
        return Privillege.get(value);
    }

    //endregion

    public RadioButton getRadioButton() {
        RadioButton radioButton = new RadioButton();
        radioButton.setSelected(isSelected());
        radioButton.selectedProperty().bindBidirectional(selectedProperty);
        return radioButton;
    }
    public void addPrivillege(String adresseMail,String typePrivillege){
        Privillege.put(adresseMail,typePrivillege);
    }

    public void addTable(Table uneTable){
        table.add(uneTable);
    }
}

