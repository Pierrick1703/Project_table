package com.example.Project_Table.controller;

import com.example.Project_Table.Modele.Colonne;
import com.example.Project_Table.Modele.Ligne;
import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.StartApplication;
import static com.example.Project_Table.StartApplication.data;

import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;


public class Excel implements Initializable {

    public static MenuItem editItem = new MenuItem("Editer");
    public static MenuItem deleteItem = new MenuItem("Supprimer");

    public static MenuItem ajoutColonne = new MenuItem("Ajout d'une colonne");

    public static ContextMenu contextMenu = new ContextMenu(editItem, deleteItem,ajoutColonne);

    @FXML private TableView<ObservableMap<String, String>> idTable = new TableView<>();
    public static String nameClickTableView;

    @FXML private TextField formulaTextField;
    @FXML public ListView<String> idList = new ListView<String>();

    @FXML public Label labelError;

    private String tableSelected;
    private String colonneSelected;
    private int ligneSelected;

    private MouseEvent lastClick = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildListView();
        eventHandlerTableView();
        formulaTextField.setOnKeyTyped(event -> onKeyTyped(event));
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
                    tableSelected = name;
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
                            int rowIndex  = getIndex();
                            int indexColumn = getTableView().getColumns().indexOf(column);
                            super.startEdit();
                            TextField textField = new TextField(getItem().toString());
                            textField.setOnKeyReleased(t -> {
                                if (t.getCode() == KeyCode.ENTER) {
                                    commitEdit(textField.getText());

                                } else if (t.getCode() == KeyCode.ESCAPE) {
                                    cancelEdit();
                                }
                            });

                            setGraphic(textField);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            textField.selectAll();
                            Platform.runLater(() -> {
                                textField.requestFocus();
                            });
                        }
                    }

                    @Override
                    public void commitEdit(String newValue) {
                        int rowIndex  = getIndex();
                        int indexColumn = getTableView().getColumns().indexOf(column);
                        super.commitEdit(newValue);
                        tableSelected.changeValueLigne(rowIndex,indexColumn,oldValue,newValue);
                        List<String> listFormule = tableSelected.getFormulaUse(rowIndex,indexColumn);
                        if(listFormule.size() >= 1){
                            formuleUpdate(tableSelected.getFormulaCell(indexColumn,rowIndex),tableSelected,"concatChange");
                        }
                        initializeTableView(tableSelected.getNom());
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
                if (selectionModel.getSelectedItem() != null && lastClick != null && lastClick.getButton() == event.getButton()) {
                    int selectedIndex = selectionModel.getSelectedIndex();
                    TableColumn<ObservableMap<String, String>, String> selectedColumn = selectionModel.getSelectedCells().get(0).getTableColumn();
                    idTable.edit(selectedIndex, selectedColumn);
                }
            }
            lastClick = event;
        });
        idTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                TableView.TableViewSelectionModel<ObservableMap<String, String>> selectionModel = idTable.getSelectionModel();
                if (selectionModel.getSelectedItem() != null) {
                    int selectedIndex = selectionModel.getSelectedIndex();
                    TableColumn<ObservableMap<String, String>, String> selectedColumn = selectionModel.getSelectedCells().get(0).getTableColumn();
                    if(selectedColumn.getText() != "Numero"){
                        formulaTextField.setText(data.getTable(tableSelected).getFormulaCell(selectedColumn.getText(),selectedIndex));
                        colonneSelected = selectedColumn.getText();
                        ligneSelected = selectedIndex;
                    }
                }
            }
        });
    }

    private void onKeyTyped(KeyEvent event) {
        if(event.getCharacter().equals("\r")) {
            if (event.getTarget() instanceof TextField) {
                TextField textField = (TextField) event.getTarget();
                String textFieldId = textField.getId();
                if("formulaTextField".equals(textFieldId)){
                    String formulaText = formulaTextField.getText();
                    ArrayList<String> tabFormule = new ArrayList<>();
                    tabFormule.addAll(Arrays.asList("Today", "Calcul", "Concat"));
                    String wordFormule = "";
                    Table currentTable = data.getTable(tableSelected);
                    int i = 0;
                    while (i < tabFormule.size()) {
                        if (formulaText.contains(tabFormule.get(i))) {
                            wordFormule = tabFormule.get(i);
                        }
                        i++;
                    }
                    switch (wordFormule) {
                        case "Today":
                            LocalDate currentDate = LocalDate.now();
                            currentTable.setValueCell(colonneSelected, ligneSelected, currentDate.toString());
                            currentTable.setFormulaCell(colonneSelected, ligneSelected, formulaText);
                            initializeTableView(tableSelected);
                            break;

                        case "Calcul":
                            break;
                        case "Concat":
                            String formulaUse = "concatFormule";//Pour savoir la provenance de l'appel
                            concat(formulaText, currentTable, formulaUse);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    public void concat(String formulaText,Table currentTable, String typeUse){
        boolean error = false;
        int j = 0;
        String formulaConcat = formulaText.substring(7,formulaText.length()-1);
        String[] tabFormulaConcat = formulaConcat.split(",");
        String resultConcat = "";
        int numberSeparator = (int) (Math.floor(Math.log10(Math.abs(currentTable.getLongestLigne()))) + 1);
        char charRecherche = '"';
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(charRecherche);
        CharSequence charSequence = stringBuilder;//Pour convertir le char de recherche en une chaine de séquence pour la fonciton contains
        while(tabFormulaConcat.length > j){
            if(tabFormulaConcat[j].contains(charSequence)){
                resultConcat += removeFirstLastChars(tabFormulaConcat[j]);
            } else {
                boolean validate = false;
                int k = 0;
                while(!validate) {
                    String nameColonne = tabFormulaConcat[j].substring(0, tabFormulaConcat[j].length() - numberSeparator + k);
                    int numberLigne = Integer.parseInt(tabFormulaConcat[j].substring(tabFormulaConcat[j].length() - numberSeparator + k, tabFormulaConcat[j].length()));
                    if (currentTable.existCell(nameColonne,numberLigne)) {
                        resultConcat += currentTable.getValueCell(nameColonne, numberLigne);
                        if(typeUse.equals("concatFormule")){
                            currentTable.getColonne(nameColonne).getLigne(numberLigne).addUsedToFormule(colonneSelected+ligneSelected);
                        }
                        validate = true;
                    }
                    if(k==tabFormulaConcat[j].length()){
                        error = true;
                        break;
                    }
                    k++;
                }
            }
            j++;
        }
        if(error){
            labelError.setText("Une erreur est présente dans la formule");
        } else{
            currentTable.setValueCell(colonneSelected,ligneSelected,resultConcat);
            currentTable.setFormulaCell(colonneSelected,ligneSelected,formulaText);
            initializeTableView(tableSelected);
        }
    }

    public static String removeFirstLastChars(String input) {
        if (input == null || input.length() <= 2) {
            return "";
        }
        return input.substring(1, input.length() - 1);
    }

    private ContextMenu createContextMenu() {
        editItem.setOnAction(event -> {
            JFrame jFrame = new JFrame();
            String getMessage = JOptionPane.showInputDialog(jFrame, "Nom de la colonne");
            //idTable.getColumns()
        });
        deleteItem.setOnAction(event -> {
            idTable.getColumns().removeIf(col -> col.getText().equals(nameClickTableView));
            idTable.refresh();
        });
        ajoutColonne.setOnAction(event -> {
            boolean validate = false;
            while(!validate) {
                JFrame jFrame = new JFrame();
                String getMessage = JOptionPane.showInputDialog(jFrame, "Nom de la colonne");
                if (StartApplication.data.getTable(tableSelected).verificationNomColonne(getMessage)) {
                    generateColonne(getMessage);
                    validate = true;
                    initializeTableView(tableSelected);
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Le nom de la colonne choisi n'est pas correct : "+getMessage);
                }
            }
        });

        return contextMenu;
    }

    private Colonne generateColonne(String name){
        Colonne colonne = null;
        ArrayList<Ligne> tabLigne = new ArrayList<Ligne>();
        Table currentTable = data.getTable(tableSelected);
        int nombreLigne = currentTable.getLongestLigne();
        for(int i = 0;i<nombreLigne;i++){
            Ligne ligne = new Ligne("",i,"");
            tabLigne.add(ligne);
        }
        colonne = new Colonne(name);
        colonne.setLigne(tabLigne);
        currentTable.addColonne(colonne);
        return colonne;
    }

    private void formuleUpdate(String formula, Table currentTable, String formulaType){
        if(formulaType.contains("concat"))
            concat(formula,currentTable,formulaType);
    }

}
