package com.example.Project_Table.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.*;

import static com.example.Project_Table.StartApplication.n;

public class Excel implements Initializable {

    public static MenuItem editItem = new MenuItem("Editer");
    public static MenuItem deleteItem = new MenuItem("Supprimer");

    public static MenuItem ajoutColonne = new MenuItem("Ajout d'une colonne");

    public static ContextMenu contextMenu = new ContextMenu(editItem, deleteItem,ajoutColonne);

    @FXML private TableView<ObservableMap<String, String>> idTable = new TableView<>();
    public static String nameClickTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i=0; i < n.getColonne().size();i++){
            String name = n.getColonne().get(i).getNom();
            TableColumn<ObservableMap<String, String>, String> column1 = new TableColumn<>(name);
            column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(name)));
            column1.setContextMenu(createContextMenu());
            final boolean isEditable = !"Numéro".equalsIgnoreCase(name);
            column1.setCellFactory(column -> {
                TextFieldTableCell<ObservableMap<String, String>, String> cell = new TextFieldTableCell<>() {
                    @Override
                    public void startEdit() {//réecriture de la fonction edition de la tableview mais en bloquant pour la colonne qui ne doit pas l'être
                        if (isEditable) {
                            super.startEdit();
                        }
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
                cell.setOnMouseClicked(event -> {//edit de la cellule
                    if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && cell.getTableView().getColumns().indexOf(cell.getTableColumn()) != 0) {
                        String cellValue = cell.getItem();
                        if(cellValue != null) {
                            cell.startEdit();
                            System.out.println("Double-clicked cell value: " + cellValue);
                        }
                    }
                });
                cell.setOnMouseClicked(event -> {//ajout d'une ligne en faisant un double-clic sur une ligne vide
                    if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                        String cellValue = cell.getItem();
                        if(cellValue == null) {
                            ObservableList<ObservableMap<String, String>> data = FXCollections.observableArrayList();
                            ObservableList<TableColumn<ObservableMap<String, String>, ?>> listColonne = idTable.getColumns();
                            for(int j=0;j<n.getLongestLigne();j++) {
                                ObservableMap<String, String> row1 = FXCollections.observableHashMap();
                                for(int k=0;k<listColonne.size();k++){
                                    if(listColonne.get(k).getText() == "Numéro"){
                                        row1.put(listColonne.get(k).getText(), String.valueOf(j));
                                    } else {
                                        row1.put(listColonne.get(k).getText(), n.getColonne().get(k).getLigne().get(j).getValeur());
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
        for(int j=0;j<n.getLongestLigne();j++) {
            ObservableMap<String, String> row1 = FXCollections.observableHashMap();
            for(int k=0;k<idTable.getColumns().size();k++){
                if(idTable.getColumns().get(k).getText() == "Numéro"){
                    row1.put(idTable.getColumns().get(k).getText(), String.valueOf(j));
                } else {
                    row1.put(idTable.getColumns().get(k).getText(), n.getColonne().get(k).getLigne().get(j).getValeur());
                }
            }
            data.add(row1);
        }
        idTable.setItems(data);
        //Event qui fonctionne bien ! Attention fonctionne avec les cellules aussi
        idTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Récupérer la colonne qui a été cliquée
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
