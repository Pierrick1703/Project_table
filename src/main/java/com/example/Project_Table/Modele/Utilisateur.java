package com.example.Project_Table.Modele;

public class Utilisateur {
    private String Nom;
    private String Prenom;
    private String AdresseMail;
    private String Privillege;
    //region Constructeurs
    public Utilisateur(String nom, String prenom, String adresseMail,String privillege){
        this.Nom = nom;
        this.Prenom = prenom;
        this.AdresseMail = adresseMail;
        this.Privillege = privillege;
    }
    public Utilisateur(String nom,String prenom,String adresseMail){
        this.Nom = nom;
        this.Prenom = prenom;
        this.AdresseMail = adresseMail;
        this.Privillege = "Utilisateur";
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
    public String getPrivillege(){
        return this.Privillege;
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
    public void setPrivillege(String privillege){
        this.Privillege = privillege;
    }
    //endregion
}
