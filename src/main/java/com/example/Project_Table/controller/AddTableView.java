package com.example.Project_Table.controller;

import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.StartApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddTableView implements Initializable {

    @FXML TextField textFieldTableName;
    @FXML TextField textField1;
    @FXML TextField textField2;
    @FXML TextField textField3;
    @FXML TextField textField4;
    @FXML TextField textField5;
    @FXML TextField textField6;
    @FXML TextField textField7;
    @FXML TextField textField8;
    @FXML TextField textField9;
    @FXML TextField textField10;
    @FXML TextField textFieldNbLigne;

    Stage StageAddTable = null;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldNbLigne.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            char c = event.getCharacter().charAt(0);
            if (!Character.isDigit(c)) {
                event.consume();
            }
        });
    }
    @FXML
    private void addTable(){
        ArrayList<TextField> tabTextFieldColonne = new ArrayList<TextField>();
        tabTextFieldColonne.add(textField1);
        tabTextFieldColonne.add(textField2);
        tabTextFieldColonne.add(textField3);
        tabTextFieldColonne.add(textField4);
        tabTextFieldColonne.add(textField5);
        tabTextFieldColonne.add(textField6);
        tabTextFieldColonne.add(textField7);
        tabTextFieldColonne.add(textField8);
        tabTextFieldColonne.add(textField9);
        tabTextFieldColonne.add(textField10);
        Table table = new Table(textFieldTableName.getText());
        StartApplication.data.addTable(table);
        String tableName = textFieldTableName.getText();
        textFieldNbLigne.getText();
        int nbLigne = 10;
        if(!textFieldNbLigne.getText().equals("") && textFieldNbLigne.getText() != null)
            nbLigne = Integer.parseInt(textFieldNbLigne.getText());
        for(TextField textFieldCurrent : tabTextFieldColonne){
            if(textFieldCurrent.getText() != "" && !table.verificationNomColonne(textFieldCurrent.getText())) {
                Colonne uneColonne = new Colonne(textFieldCurrent.getText());
                table.addColonne(uneColonne);
                ArrayList<Ligne> listLigne = new ArrayList<>();
                for(int i=0;i<nbLigne;i++){
                    Ligne uneLigne = new Ligne("",i,"");
                    listLigne.add(uneLigne);
                }
                uneColonne.setLigne(listLigne);
            }
        }

    }

    @FXML
    private void cancelAddTable(){
        StageAddTable.close();
    }

    public void initialize(Stage stageAddTable) {
        StageAddTable = stageAddTable;
    }
}
