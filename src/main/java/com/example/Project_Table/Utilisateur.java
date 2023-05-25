package com.example.Project_Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;

public class Utilisateur {
    private String Nom;
    private String Prenom;
    private String AdresseMail;
    private String Télephone;
    private String Motdepasse;
    private String Matricule;
    private Groupe groupe1;
    private Groupe groupe2;
    private Groupe groupe3;
    private String privilege1;
    private String  privilege2;
    private String  privilege3;
    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    // constructeurs

    public Utilisateur(String nom, String prenom, String adresseMail, String télephone, String motdepasse, String matricule, Groupe groupe1, Groupe groupe2, Groupe groupe3, String  privilege1, String  privilege2, String  privilege3) {
        Nom = nom;
        Prenom = prenom;
        AdresseMail = adresseMail;
        Télephone = télephone;
        Motdepasse = motdepasse;
        Matricule = matricule;
        this.groupe1 = groupe1;
        this.groupe2 = groupe2;
        this.groupe3 = groupe3;
        this.privilege1 = privilege1;
        this.privilege2 = privilege2;
        this.privilege3 = privilege3;
    }

    public Utilisateur(String nom, String prenom, String adresseMail, String télephone, String motdepasse, String matricule) {
        Nom = nom;
        Prenom = prenom;
        AdresseMail = adresseMail;
        Télephone = télephone;
        Motdepasse = motdepasse;
        Matricule = matricule;
    }

    public Utilisateur(String nom, String prenom, String adresseMail, String télephone, String motdepasse, String matricule, Groupe groupe1, String  privilege1) {
        Nom = nom;
        Prenom = prenom;
        AdresseMail = adresseMail;
        Télephone = télephone;
        Motdepasse = motdepasse;
        Matricule = matricule;
        this.groupe1 = groupe1;
        this.privilege1 = privilege1;
    }

    public Utilisateur(String nom, String prenom, String adresseMail, String télephone, String motdepasse, String matricule, Groupe groupe1, Groupe groupe2, String  privilege1, String  privilege2) {
        Nom = nom;
        Prenom = prenom;
        AdresseMail = adresseMail;
        Télephone = télephone;
        Motdepasse = motdepasse;
        Matricule = matricule;
        this.groupe1 = groupe1;
        this.groupe2 = groupe2;
        this.privilege1 = privilege1;
        this.privilege2 = privilege2;
    }

    // getters
    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getAdresseMail() {
        return AdresseMail;
    }

    public String getTélephone() {
        return Télephone;
    }

    public String getMotdepasse() {
        return Motdepasse;
    }

    public String getMatricule() {
        return Matricule;
    }

    public Groupe getGroupe1() {
        return groupe1;
    }

    public Groupe getGroupe2() {
        return groupe2;
    }

    public Groupe getGroupe3() {
        return groupe3;
    }

    public String  getPrivilege1() {
        return privilege1;
    }

    public String  getPrivilege2() {
        return privilege2;
    }

    public String  getPrivilege3() {
        return privilege3;
    }

    //Setters
    public void setNom(String nom) {
        Nom = nom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public void setAdresseMail(String adresseMail) {
        AdresseMail = adresseMail;
    }

    public void setTélephone(String télephone) {
        Télephone = télephone;
    }

    public void setMotdepasse(String motdepasse) {
        Motdepasse = motdepasse;
    }

    public void setMatricule(String matricule) {
        Matricule = matricule;
    }

    public void setGroupe1(Groupe groupe1) {
        this.groupe1 = groupe1;
    }

    public void setGroupe2(Groupe groupe2) {
        this.groupe2 = groupe2;
    }

    public void setGroupe3(Groupe groupe3) {
        this.groupe3 = groupe3;
    }

    public void setPrivilege1(String  privilege1) {
        this.privilege1 = privilege1;
    }

    public void setPrivilege2(String  privilege2) {
        this.privilege2 = privilege2;
    }

    public void setPrivilege3(String  privilege3) {
        this.privilege3 = privilege3;
    }

    public void setSelected(boolean selected) {
        selectedProperty.set(selected);
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public RadioButton getRadioButton() {
        RadioButton radioButton = new RadioButton();
        radioButton.setSelected(isSelected());
        radioButton.selectedProperty().bindBidirectional(selectedProperty);
        return radioButton;
    }
}
