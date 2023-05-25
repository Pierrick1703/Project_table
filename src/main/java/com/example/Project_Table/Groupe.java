package com.example.Project_Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;

public class Groupe {
    private String Nom;
    private String Departement;
    private int Nombre_utilisateur;
    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    public Groupe(String nom, String departement, int nombre_utilisateur) {
        this.Nom = nom;
        this.Departement = departement;
        this.Nombre_utilisateur = nombre_utilisateur;
    }

    public Groupe(String nom, String departement) {
        this.Nom = nom;
        this.Departement = departement;
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

    public String getNom() {
        return Nom;
    }

    public String getDepartement() {
        return Departement;
    }

    public int getNombre_utilisateur() {
        return Nombre_utilisateur;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public void setDepartement(String departement) {
        Departement = departement;
    }

    public void setNombre_utilisateur(int nombre_utilisateur) {
        Nombre_utilisateur = nombre_utilisateur;
    }

    public RadioButton getRadioButton() {
        RadioButton radioButton = new RadioButton();
        radioButton.setSelected(isSelected());
        radioButton.selectedProperty().bindBidirectional(selectedProperty);
        return radioButton;
    }
}
