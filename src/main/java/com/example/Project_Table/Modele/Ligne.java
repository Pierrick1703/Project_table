package com.example.Project_Table.Modele;

public class Ligne {

    private String Valeur;

    private Integer Numero;

    private String Forumle;

    public Ligne(String valeur, Integer numero, String formule){
        this.Valeur = valeur;
        this.Numero = numero;
        this.Forumle = formule;
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
}
