package com.example.Project_Table;

import com.example.Project_Table.BDD.Database;
import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartApplication extends Application {

    public static Database data = new Database();
    //public static Stage stageAddTable = new Stage();
    @Override
    public void start(Stage stage) throws IOException {
        data.fetchQuery();
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