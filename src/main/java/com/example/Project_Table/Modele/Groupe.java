package com.example.Project_Table.Modele;

import java.util.List;

public class Groupe {
    private String Nom;
    private List<Table> DroitLecture;
    private List<Table> DroitEcriture;
    private Administration DroitAdministration;

    //region Constructeur
    public Groupe(String nom){
        this.Nom = nom;
    }
    //endregion
    //region Getter and setter
    public String getNom(){
        return Nom;
    }
    public List<Table> getDroitLecture(){
        return DroitLecture;
    }
    public List<Table> getDroitEcriture(){
        return DroitEcriture;
    }
    public Administration getDroitAdministration(){
        return DroitAdministration;
    }
    public void setNom(String nom){
        this.Nom = nom;
    }
    public void setDroitLecture(List<Table> droitLecture){
        this.DroitLecture = droitLecture;
    }
    public void setDroitEcriture(List<Table> droitEcriture){
        this.DroitEcriture = droitEcriture;
    }
    public void setDroitAdministration(Administration droitAdministration){
        this.DroitAdministration = droitAdministration;
    }
    //endregion
}

