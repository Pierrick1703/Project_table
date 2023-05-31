package com.example.Project_Table.Modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.Random;

public class Utilisateur {

    //region Variable
    private String Nom;
    private String Prenom;
    private String AdresseMail;

    private String Telephone;

    private String motDePasse;

    private ArrayList<Groupe> Groupe;

    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    private  Random random = new Random();
    //endregion
    //region Constructeurs
    public Utilisateur(String nom, String prenom, String adresseMail,String telephone, String motDePasse){
        this.Nom = nom;
        this.Prenom = prenom;
        this.AdresseMail = adresseMail;
        this.Telephone = telephone;
        this.motDePasse = motDePasse;
    }
    public Utilisateur(String nom, String prenom, String adresseMail, String motDePasse){
        this.Nom = nom;
        this.Prenom = prenom;
        this.AdresseMail = adresseMail;
        this.Telephone = null;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String nom, String prenom,String telephone, String adresseMail,ArrayList<Groupe> group){
        this.Nom = nom;
        this.Prenom = prenom;
        this.AdresseMail = adresseMail;
        this.Telephone = telephone;
        this.motDePasse = randomize();
        this.Groupe = group;
    }

    //endregion
    //region Getter and setter
    public String getNom(){
        return this.Nom;
    }
    public String getPrenom(){
        return this.Prenom;
    }
    public String getAdresseMail(){
        return this.AdresseMail;
    }
    public String getTelephone(){
        return this.Telephone;
    }
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom(String nom){
        this.Nom = nom;
    }
    public void setPrenom(String prenom){
        this.Prenom = prenom;
    }
    public void setAdresseMail(String adresseMail){
        this.AdresseMail = adresseMail;
    }
    public void setTelephone(String telephone) {
        Telephone = telephone;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public ArrayList<Groupe> getGroupe() {
        return Groupe;
    }

    public void setGroupe(ArrayList<Groupe> groupe) {
        Groupe = groupe;
    }
//endregion

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public String randomize(){
        String CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String SMALL_CHARS = "abcdefghijklmnopqrstuvwxyz";
        String NUMBERS = "0123456789";
        String SPECIAL_CHARS = "!@#$%^&*()_-+=<>?/{}~|";
        int STRING_LENGTH = 6;
        StringBuilder sb = new StringBuilder();
        sb.append(getRandomCharacter(CAPITAL_CHARS));
        sb.append(getRandomCharacter(SMALL_CHARS));
        sb.append(getRandomCharacter(NUMBERS));
        sb.append(getRandomCharacter(SPECIAL_CHARS));

        String allPossibleChars = CAPITAL_CHARS + SMALL_CHARS + NUMBERS + SPECIAL_CHARS;
        while (sb.length() < STRING_LENGTH) {
            sb.append(getRandomCharacter(allPossibleChars));
        }
        return sb.toString();
    }

    private char getRandomCharacter(String input) {
        return input.charAt(random.nextInt(input.length()));
    }

    public RadioButton getRadioButton() {
        RadioButton radioButton = new RadioButton();
        radioButton.setSelected(isSelected());
        radioButton.selectedProperty().bindBidirectional(selectedProperty);
        return radioButton;
    }

    public void setSelected(boolean selected) {
        selectedProperty.set(selected);
    }
}
