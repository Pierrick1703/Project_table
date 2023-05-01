package com.example.Project_Table.controller;

import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.StartApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.Project_Table.StartApplication.data;

public class Excel implements Initializable {

    public static MenuItem editItem = new MenuItem("Editer");
    public static MenuItem deleteItem = new MenuItem("Supprimer");

    public static MenuItem ajoutColonne = new MenuItem("Ajout d'une colonne");

    public static ContextMenu contextMenu = new ContextMenu(editItem, deleteItem,ajoutColonne);

    @FXML private TableView<ObservableMap<String, String>> idTable = new TableView<>();
    public static String nameClickTableView;

    @FXML private TextField formulaTextField;
    @FXML public ListView<String> idList = new ListView<String>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildListView();
        eventHandlerTableView();
    }

    public void buildListView(){
        StartApplication.data.createObservableList();
        idList.setItems(StartApplication.data.getItems());
        idList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                String name = removeFirstLastChars(idList.getSelectionModel().getSelectedItems().toString());
                if(name.equals("+ Nouvelle table")){
                    try {
                        Stage stageAddTable = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("AddTableView.fxml"));
                        Parent root = fxmlLoader.load();
                        AddTableView addTableController = fxmlLoader.getController();
                        addTableController.initialize(stageAddTable);
                        Scene scene = new Scene(root, 520, 360);
                        stageAddTable.setTitle("Ajout d'une table");
                        stageAddTable.setOnHidden(event2 -> {
                            this.buildListView();
                        });
                        stageAddTable.setScene(scene);
                        stageAddTable.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    initializeTableView(name);
                }
            }
        });
    }

    private void initializeTableView(String nameTable){
        idTable.getColumns().clear();
        Table tableSelected = data.getTable(nameTable);
        idTable.setEditable(true);
        for(int i=0; i < tableSelected.getColonne().size();i++){
            String name = tableSelected.getColonne().get(i).getNom();
            TableColumn<ObservableMap<String, String>, String> column1 = new TableColumn<>(name);
            column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(name)));
            column1.setContextMenu(createContextMenu());
            final boolean isEditable = !"Numéro".equalsIgnoreCase(name);
            column1.setCellFactory(column -> {
                TextFieldTableCell<ObservableMap<String, String>, String> cell = new TextFieldTableCell<>() {
                    private String oldValue;
                    private int indexColumn;

                    @Override
                    public void startEdit() {
                        if (isEditable) {
                            oldValue = getItem();
                            super.startEdit();
                        }
                    }

                    @Override
                    public void commitEdit(String newValue) {
                        int rowIndex  = getIndex();
                        int indexColumn = getTableView().getColumns().indexOf(column);
                        super.commitEdit(newValue);
                        System.out.println("Index de la Colonne: " + indexColumn);
                        System.out.println("Index de la ligne: " + rowIndex);
                        System.out.println("Ancienne valeur: " + oldValue);
                        System.out.println("Nouvelle valeur: " + newValue);
                        tableSelected.changeValueLigne(rowIndex,indexColumn,oldValue,newValue);
                        System.out.println(tableSelected.toJson());
                    }
                };
                cell.setConverter(new StringConverter<String>() {
                    @Override
                    public String toString(String object) {
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
                /*cell.setOnMouseClicked(event -> {//edit de la cellule - obsolete pour être remplacer par la fonction startEdit ré-écrit
                    if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && cell.getTableView().getColumns().indexOf(cell.getTableColumn()) != 0) {
                        String cellValue = cell.getItem();
                        if(cellValue != null) {
                            cell.startEdit();
                            System.out.println("Double-clicked cell value: " + cellValue);
                        }
                    }
                });*/
                cell.setOnMouseClicked(event -> {//ajout d'une ligne en faisant un double-clic sur une ligne vide
                    if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                        String cellValue = cell.getItem();
                        if(cellValue == null) {
                            ObservableList<ObservableMap<String, String>> data = FXCollections.observableArrayList();
                            ObservableList<TableColumn<ObservableMap<String, String>, ?>> listColonne = idTable.getColumns();
                            for(int j=0;j<tableSelected.getLongestLigne();j++) {
                                ObservableMap<String, String> row1 = FXCollections.observableHashMap();
                                for(int k=0;k<listColonne.size();k++){
                                    if(listColonne.get(k).getText() == "Numéro"){
                                        row1.put(listColonne.get(k).getText(), String.valueOf(j));
                                    } else {
                                        row1.put(listColonne.get(k).getText(), tableSelected.getColonne().get(k).getLigne().get(j).getValeur());
                                    }
                                }
                                data.add(row1);
                            }
                            for(int k=0;k<listColonne.size();k++){
                                ObservableMap<String, String> row1 = FXCollections.observableHashMap();
                                if(listColonne.get(k).getText() == "Numéro"){
                                    row1.put(listColonne.get(k).getText(),String.valueOf(data.size()+1));
                                }else{
                                    row1.put(listColonne.get(k).getText(), "");
                                }
                                data.add(row1);
                            }
                            idTable.setItems(data);
                        }
                    }
                });
                return cell;
            });
            column1.setEditable(true);
            idTable.getColumns().add(column1);
        }
        ObservableList<ObservableMap<String, String>> data = FXCollections.observableArrayList();
        for(int j=0;j<tableSelected.getLongestLigne();j++) {
            ObservableMap<String, String> row1 = FXCollections.observableHashMap();
            for(int k=0;k<idTable.getColumns().size();k++){
                if(idTable.getColumns().get(k).getText() == "Numéro"){
                    row1.put(idTable.getColumns().get(k).getText(), String.valueOf(j));
                } else {
                    row1.put(idTable.getColumns().get(k).getText(), tableSelected.getColonne().get(k).getLigne().get(j).getValeur());
                }
            }
            data.add(row1);
        }
        idTable.setItems(data);
    }

    private void eventHandlerTableView(){
        idTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                EventTarget target = event.getTarget();
                if (target instanceof Text) {
                    nameClickTableView = ((Text) target).getText();
                }
            }
        });
        idTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                TableView.TableViewSelectionModel<ObservableMap<String, String>> selectionModel = idTable.getSelectionModel();
                if (selectionModel.getSelectedItem() != null) {
                    int selectedIndex = selectionModel.getSelectedIndex();
                    TableColumn<ObservableMap<String, String>, String> selectedColumn = selectionModel.getSelectedCells().get(0).getTableColumn();
                    idTable.edit(selectedIndex, selectedColumn);
                }
            }
        });
    }
    public static String removeFirstLastChars(String input) {
        if (input == null || input.length() <= 2) {
            return "";
        }
        return input.substring(1, input.length() - 1);
    }

    private ContextMenu createContextMenu() {
        editItem.setOnAction(event -> {
            System.out.println("Valeur texte : " + nameClickTableView);
        });
        deleteItem.setOnAction(event -> {
            idTable.getColumns().removeIf(col -> col.getText().equals(nameClickTableView));
            idTable.refresh();
            System.out.println(nameClickTableView);
        });
        ajoutColonne.setOnAction(event -> {
            TableColumn<ObservableMap<String, String>, String> column1 = new TableColumn<>("");
            column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("")));
            column1.setContextMenu(createContextMenu());
            idTable.getColumns().add(column1);
            System.out.println("Valeur texte : " + nameClickTableView);
        });

        return contextMenu;
    }
}
