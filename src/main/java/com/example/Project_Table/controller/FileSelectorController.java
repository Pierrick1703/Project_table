package com.example.Project_Table.controller;
import com.example.Project_Table.BDD.Database;
import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.StartApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSelectorController {
    private File selectedFile;

    @FXML
    private TextField fileNameTextField;
    @FXML
    private TableView table;
    @FXML
    private TextField dbNameTextField;

    private void loadTable() {
        // Clear previous table data
        table.getColumns().clear();
        table.getItems().clear();

        // Add columns
        for (int i = 1; i <= 4; i++) {
            TableColumn<String[], String> col = new TableColumn<>("Nom de la colonne " + i);
            final int colIndex = i - 1;

            col.setCellValueFactory(cellData -> {
                String[] row = cellData.getValue();
                if (row != null && colIndex < row.length) {
                    return new SimpleStringProperty(row[colIndex]);
                } else {
                    return new SimpleStringProperty();
                }
            });

            col.setCellFactory(TextFieldTableCell.forTableColumn());

            col.setOnEditCommit(event -> {
                String[] row = event.getTableView().getItems().get(event.getTablePosition().getRow());
                row[colIndex] = event.getNewValue();
            });

            table.getColumns().add(col);
        }

        // Add data rows
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            int rowNumber = 0;

            while ((line = br.readLine()) != null && rowNumber < 10000) { // Limiter à 100 lignes pour des raisons de performance
                String[] row = line.split(";");
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim();
                }
                table.getItems().add(row);
                rowNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier");
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            fileNameTextField.setText(selectedFile.getName());
            loadTable();
        }
    }

    public void createTable(ActionEvent actionEvent) {
        // ...

        // Add data rows
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            int rowNumber = 0;
            Table Pable = new Table(dbNameTextField.getText());
            Pable.setEcriture(true);
            Pable.setLecture(false);
            StartApplication.data.addTable(Pable);
            for (int i = 0; i < table.getItems().size(); i++) {
                table.getItems().get(i).toString();
                List<String> MaListe = new ArrayList<>();
                int nbColonne = table.getColumns().size();

                for (int j = 0; j < nbColonne; j++) {
                    Ligne lign = null;
                    Object Item = table.getItems().get(i);
                    String[] row = (String[]) Item;
                    String Value = row[j];
                    if (i == 0) {
                        Colonne Macolonne = new Colonne(Value.toString());
                        Pable.addColonne(Macolonne);
                    } else {
                        if(j == 0){
                            lign = new Ligne(Integer.toString(i), i, "");
                            Pable.getColonne(j).AddLigne(lign);
                        }
                            lign = new Ligne(Value.toString(), i, "");
                            Pable.getColonne(j+1).AddLigne(lign);
                    }
                }
            }
            insertDataIntoDatabase(Pable.getNom(), Pable.toJson());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void insertDataIntoDatabase (String nomTable, JSONObject StringJson){
        // Connect to the database
        try (Connection connection = DriverManager.getConnection("jdbc:mariadb://109.234.162.158:3306/gocl4291_wegrow_sandbox", "gocl4291_pcottin", "*YF!b+BjsUey")) {
            // Prepare the SQL statement
            String sql = "insert into dataTable (nameTable,jsonTable,ViewUserId,EditUserId,AdminUserId) values ('"+nomTable+"','"+ StringJson.toString().replace("\\","\\\\")+"',null,null,null)";
            System.out.println(sql);
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void goToExcel(ActionEvent actionEvent){
        try {
            // Charger le fichier FXML du menu admin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("excel.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour afficher le menu admin
            Scene scene = new Scene(root);

            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Afficher la scène dans la fenêtre actuelle
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
