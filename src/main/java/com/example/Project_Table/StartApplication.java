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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {

    public static Database data = new Database();
    //public static Stage stageAddTable = new Stage();
    @Override
    public void start(Stage stage) throws IOException {
        Utilisateur utilisateur1 = new Utilisateur("COTTIN","Pierrick","cottin.pierrick@gmail.com");
        data.fetchQuery();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("excel.fxml"));
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
        stage.setTitle("New Table");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setOnHidden(event2 -> {
            data.save();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}