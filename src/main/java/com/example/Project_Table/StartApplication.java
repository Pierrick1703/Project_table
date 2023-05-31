package com.example.Project_Table;

import com.example.Project_Table.BDD.Database;
import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.Modele.Utilisateur;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {

    public static Database data = new Database();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("PierTable-APPLICATION");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}