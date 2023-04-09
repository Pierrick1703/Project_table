package com.example.Project_Table;

import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {

    public static Table t = new Table("Table de test");
    public static Table m = new Table("Table de travail");
    public static Table n = new Table("Compta");
    public static Colonne colonne1 = new Colonne("Nom","String");
    public static Colonne colonne2 = new Colonne("Prenom","String");
    public static Colonne colonne3 = new Colonne("numéro","Int");
    public static Ligne l1 = new Ligne("Azerty",1,"");
    public static Ligne l2 = new Ligne("Qwerty",2,"");
    public static Ligne l3 = new Ligne("test",3,"");

    public static Ligne l4 = new Ligne("PAzerty",1,"");
    public static Ligne l5 = new Ligne("PQwerty",2,"");
    public static Ligne l6 = new Ligne("Ptest",3,"");

    public static Ligne l7 = new Ligne("1",1,"");
    public static Ligne l8 = new Ligne("2",2,"");
    public static Ligne l9 = new Ligne("3",3,"");
    @Override
    public void start(Stage stage) throws IOException {
        List<Ligne> tabL1 = new ArrayList<Ligne>();
        tabL1.add(l1);
        tabL1.add(l2);
        tabL1.add(l3);
        List<Ligne> tabL2 = new ArrayList<Ligne>();
        tabL2.add(l4);
        tabL2.add(l5);
        tabL2.add(l6);
        List<Ligne> tabL3 = new ArrayList<Ligne>();
        tabL3.add(l7);
        tabL3.add(l8);
        tabL3.add(l9);
        colonne1.setLigne(tabL1);
        colonne2.setLigne(tabL2);
        colonne3.setLigne(tabL3);
        n.addColonne(colonne1);
        n.addColonne(colonne2);
        n.addColonne(colonne3);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("excel.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 360);
        stage.setTitle("New Table");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}