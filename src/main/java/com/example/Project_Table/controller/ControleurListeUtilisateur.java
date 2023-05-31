package com.example.Project_Table.controller;

import com.example.Project_Table.Modele.Groupe;
import com.example.Project_Table.Modele.Utilisateur;
import com.example.Project_Table.StartApplication;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class ControleurListeUtilisateur implements Initializable {

    //region Variable global
    @FXML
    private AnchorPane PageCreationGroupe;

    @FXML
    private Label nomidentifiantadminOut;

    @FXML
    private Hyperlink LienPageDeconnexion;

    @FXML
    private Hyperlink pageCreationutilisateurlien;

    @FXML
    private Hyperlink pageCreationGroupelien;

    @FXML
    private Hyperlink MenuImporterListeUtilisateurlienCSV;

    @FXML
    private Hyperlink pageCreationutilisaMenuExporterListeUtilisateurlien2;

    @FXML
    private Hyperlink pageCreationutilisaMenuImporterListeUtilisateurlienTXT;

    @FXML
    private Hyperlink LienPageAccueil;

    @FXML
    private TableView<Utilisateur> TableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, RadioButton> ColonneSelectutilisateur;

    @FXML
    private RadioButton ColonneSelectduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonneMatriculeduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonneNomduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonnePrénomduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonneTelduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonneEmailduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, Groupe> ColonneGroupeduTableauUtilisateur;

    @FXML
    private TableColumn<Utilisateur, String> ColonneTypeutilisateurduTableauUtilisateur;

    @FXML
    private TextArea ChamprechercherdansletableauUtilisateurIn;

    @FXML
    private Button ModifierUtilisateurdanslalisteBouton;

    @FXML
    private Button SupprimerUtilisateurdanslalisteBouton;

    @FXML
    private Button SupprimerdoublonsUtilisateurdanslalisteBouton;

    @FXML
    private Pagination DefilerPageUtilisateurIn;

    @FXML
    private Label TotaldesEnregistrementutilisateur;

    private int elementsParPage = 3; // Remplacez par le nombre d'éléments souhaité par page
    private ObservableList<Utilisateur> utilisateurObservableList; // on creer une liste observable d'u qui nous permettra de pouvoir voir tous les modifications de groupe et de pouvoir etre utilisé par l'ensembles des fonctions
    ArrayList<Utilisateur> utilisateursSelectionnes = new ArrayList<>(); // Elle permet de stocker et de manipuler des object groupe(add, delete).
    ArrayList<Utilisateur> utilisateurArrayList = new ArrayList<>();
    ObservableList<Utilisateur> utilisateurtab;

    //endregion

    public ObservableList<Utilisateur> observableList() {
        //pour radio bouton
        utilisateurtab = FXCollections.observableArrayList(utilisateur -> new Observable[]{utilisateur.selectedProperty()});
        utilisateurtab.addAll(utilisateurArrayList);
        return utilisateurtab;
    }

    public void initialize(URL location, ResourceBundle resources) {
        ColonneNomduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("Nom"));
        ColonneTelduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("Telephone"));
        ColonneEmailduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("Email"));
        ColonnePrénomduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("Prenom"));
        ColonneGroupeduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, Groupe>("Groupe"));
        ColonneTypeutilisateurduTableauUtilisateur.setCellValueFactory(new PropertyValueFactory<Utilisateur, String>("Type_utilisateur"));

        List<Utilisateur> list = StartApplication.data.fetchUtilisateur();
        ObservableList<Utilisateur> observableList = FXCollections.observableArrayList(list);
        TableauUtilisateur.setItems(observableList);
        TableauUtilisateur.setItems(observableList());
        // pour le radiobouton
        ColonneSelectutilisateur.setCellValueFactory(cellData -> {
            Utilisateur utilisateur = cellData.getValue();

            RadioButton radioButton = new RadioButton();
            radioButton.setSelected(utilisateur.isSelected());
            radioButton.selectedProperty().bindBidirectional(utilisateur.selectedProperty());

            return new SimpleObjectProperty<>(radioButton);
        });

        // Calculer le nombre total de pages
        int totalEnregistrements = utilisateurArrayList.size();
        int totalPages = (int) Math.ceil((double) totalEnregistrements / elementsParPage);

        // Limiter le nombre de pages affichées dans la barre de défilement
        DefilerPageUtilisateurIn.setPageCount(totalPages);

        // Initialiser le tableau avec le nombre initial d'éléments par page
        updateTable(0);

        // Réagir aux changements de page
        DefilerPageUtilisateurIn.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateTable(newIndex.intValue());
        });

        // Mettre à jour le total des enregistrements
        mettreAJourTotalEnregistrements();
    }

    public void updateTable(int pageIndex) {
        int startIndex = pageIndex * elementsParPage;
        int endIndex = Math.min(startIndex + elementsParPage, utilisateurArrayList.size());
        List<Utilisateur> elementsAffiches = utilisateurArrayList.subList(startIndex, endIndex);
        utilisateurObservableList = FXCollections.observableArrayList(elementsAffiches);
        TableauUtilisateur.setItems(utilisateurObservableList);
    }

    // Méthode pour générer le contenu du tableau au format CSV
    private String generateCSVContent() {
        StringBuilder csvContent = new StringBuilder();
        // Ajouter les en-têtes des colonnes
        csvContent.append("Nom, Prénom, Telephone, Adresse_Mail, groupe1, Type_utilisateur1, groupe2, Type_utilisateur2, groupe3, Type_utilisateur3, \n");
        // Parcourir la liste des groupes et ajouter les données au contenu CSV
        for (Utilisateur utilisateur : utilisateurArrayList) {
            csvContent.append(utilisateur.getNom()).append(",");
            csvContent.append(utilisateur.getPrenom()).append(",");
            csvContent.append(utilisateur.getTelephone()).append(",");
            csvContent.append(utilisateur.getAdresseMail()).append(",");
            for(Groupe currentGroup : utilisateur.getGroupe()){
                csvContent.append(currentGroup.getNom()).append(",");
                csvContent.append(currentGroup.getPrivillege(utilisateur.getNom().toString())).append(",");
            }
            csvContent.append("\n");

        }
        return csvContent.toString();
    }

    @FXML
    void ExporterFicherUtilisateurAction(ActionEvent event) {
        // Générer le contenu CSV
        String csvContent = generateCSVContent();

        // Ouvrir une boîte de dialogue pour sélectionner l'emplacement de sauvegarde du fichier CSV
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter le tableau en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Écrire le contenu CSV dans le fichier sélectionné
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(csvContent);
                fileWriter.close();
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export réussi");
                alert.setHeaderText(null);
                alert.setContentText("Le tableau a été exporté avec succès.");
                alert.showAndWait();
            } catch (IOException e) {
                // Afficher un message d'erreur en cas d'échec de l'exportation
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'exportation");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de l'exportation du tableau.");
                alert.showAndWait();
            }
        }
    }

    private List<Utilisateur> obtenirTableauUtilisateurs() {
        // Retournez le contenu actuel du tableau groupetab ou de toute autre source de données
        return new ArrayList<>(utilisateurtab);
    }

    @FXML
    void ImporterFicherUtilisateurCSVAction(ActionEvent event) {
        // Ouvrir une boîte de dialogue pour sélectionner le fichier CSV à importer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un fichier CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Lire le contenu du fichier CSV
                List<String> lines = Files.readAllLines(file.toPath());
                // Supprimer l'en-tête du fichier CSV (première ligne)
                lines.remove(0);

                // Créer une nouvelle liste pour stocker les groupes importés
                List<Utilisateur> utilisateursImportes = new ArrayList<>();

                // Parcourir les lignes du fichier CSV
                for (String line : lines) {
                    // Diviser la ligne en colonnes en utilisant la virgule comme séparateur
                    String[] columns = line.split(",");
                    ArrayList<String> GroupeName = new ArrayList<>();
                    ArrayList<String> GroupeStatus = new ArrayList<>();
                    ArrayList<Groupe> lesGroupes = new ArrayList<>();
                    Groupe groupe = null;
                    if (columns.length >=7 ) {
                        String Nom = columns[1].trim();
                        String Prenom = columns[2].trim();
                        String Telephone = columns[3].trim();
                        String  Email = columns[4].trim();
                        int i =5;
                        while(i< columns.length){
                            if(!columns[i].equals("") && !columns[i+1].equals("")){
                                GroupeName.add(columns[i].toString());
                                GroupeStatus.add(columns[i].toString());
                            }
                            i=i+2;
                        }

                        // Vérifier si les informations du groupe sont valides
                        boolean NomValide = verificationNometPrenomUtilisateur(Nom);
                        boolean PrenomValide = verificationNometPrenomUtilisateur(Prenom);
                        boolean TelephoneValide = verificationTelephone(Telephone);
                        boolean EmailValide = verificationadressemail(Email);

                        if (NomValide && PrenomValide && TelephoneValide && EmailValide) {
                            // Créer un nouvel objet Groupe avec les informations du fichier CSV
                            int j = 0;
                            while(j < GroupeName.size()){
                                Map<String,String> privillege = new HashMap<>();
                                privillege.put(Email,GroupeStatus.get(j));
                                groupe = new Groupe(GroupeName.get(j),privillege);
                                lesGroupes.add(groupe);
                                j++;
                            }
                            Utilisateur utilisateur = new Utilisateur(Nom,Prenom,Telephone,Email, lesGroupes);
                            utilisateursImportes.add(utilisateur);
                            StartApplication.data.addUtilisateur(utilisateur);
                        }
                    }
                }

                if (utilisateursImportes.isEmpty()) {
                    // Afficher un message d'erreur si aucune liste valide n'a été importée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur d'importation");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucune liste valide n'a été importée.");
                    alert.showAndWait();
                    return;
                }

                // Afficher une boîte de dialogue pour demander à l'utilisateur s'il souhaite ajouter ou écraser la liste existante
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Souhaitez-vous ajouter la liste importée à celle existante ou remplacer la liste existante ?");
                ButtonType addButton = new ButtonType("Ajouter");
                ButtonType replaceButton = new ButtonType("Remplacer");
                ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(addButton, replaceButton, cancelButton);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == addButton) {
                        // Ajouter les groupes importés à la liste existante
                        utilisateurArrayList.addAll(utilisateursImportes);
                    } else if (result.get() == replaceButton) {
                        // Remplacer la liste existante par les groupes importés
                        utilisateurArrayList.clear();
                        StartApplication.data.clearUtilisateur();
                        utilisateurArrayList.addAll(utilisateursImportes);
                        StartApplication.data.addAllUtilisateur(utilisateursImportes);
                    }
                }

                // Mettre à jour le tableau avec les nouveaux enregistrements
                updateTable(DefilerPageUtilisateurIn.getCurrentPageIndex());

                // Mettre à jour le total des enregistrements
                mettreAJourTotalEnregistrements();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Import réussi");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le fichier a été importé avec succès.");
                successAlert.showAndWait();
            } catch (IOException e) {
                // Afficher un message d'erreur en cas d'échec de l'importation
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur d'importation");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur s'est produite lors de l'importation du fichier.");
                errorAlert.showAndWait();
            }
        }
    }


    // Vérifie nom et prénom
    boolean verificationNometPrenomUtilisateur(String infoutilisateur) {
        boolean isValid = infoutilisateur != null && !infoutilisateur.isEmpty() && infoutilisateur.length() <= 50;
        return isValid;
    }
    // Vérifie email
    boolean verificationadressemail(String identifiant){
        // On utilise une expression régulière pour valider l'adresse mail
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValid = identifiant.matches(emailRegex);
        return isValid;
    }
    // Vérifie motdepasse
    boolean verificationmotdepasse(String motdepasse){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        boolean isValid = motdepasse.matches(passwordRegex);
        return isValid;
    }
    // Vérifie téléphone
    boolean verificationTelephone(String telephone) {
        // Supprimer les espaces et les caractères spéciaux du numéro de téléphone
        String numeroNettoye = telephone.replaceAll("[^0-9]", "");

        // Vérifier la longueur du numéro de téléphone
        if (numeroNettoye.length() != 10) {
            return false;
        }

        // Vérifier si le numéro de téléphone commence par "07" ou "06"
        String prefixe1 = "07";
        String prefixe2 = "06";
        if (!numeroNettoye.startsWith(prefixe1) && !numeroNettoye.startsWith(prefixe2)) {
            return false;
        }

        return true;
    }

    @FXML
    void ImporterFicherUtilisateurTXTAction(ActionEvent event) {

    }

    @FXML
    void ModifierUtilisateur(ActionEvent event) {
        // Vérifier si un groupe a été sélectionné
        Utilisateur utilisateurSelectionne = null;
        for (Utilisateur utilisateur : utilisateurArrayList) {
            if (utilisateur.isSelected()) {
                if (utilisateurSelectionne == null) {
                    utilisateurSelectionne = utilisateur;
                } else {
                    // Plusieurs groupes sélectionnés, afficher un message à l'utilisateur
                    String message = "Veuillez sélectionner uniquement l'utilisateur à modifier.";
                    // Afficher le message dans une boîte de dialogue
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText(message);
                    alert.showAndWait();
                    return;
                }
            }
        }
    }

    @FXML
    void SelectionnerToututilisateurAction(ActionEvent event) {
        if (ColonneSelectduTableauUtilisateur.isSelected()) {
            // Mode de sélection "All" : Sélectionner tous les RadioButton de la colonne colonneSelectgroupe
            for (Utilisateur utilisateur : utilisateurArrayList) {
                utilisateur.setSelected(true);
                utilisateursSelectionnes.add(utilisateur);
            }
        } else {
            boolean selectionMultiple = false;
            boolean selectAll = true;

            for (Utilisateur utilisateur : utilisateurArrayList) {
                if (utilisateur.getRadioButton().isSelected()) {
                    // Au moins un RadioButton est sélectionné
                    selectAll = false;

                    if (selectionMultiple) {
                        // Mode de sélection "Multiple" : Garder la sélection actuelle des RadioButtons
                        utilisateur.setSelected(utilisateur.getRadioButton().isSelected());
                        utilisateursSelectionnes.add(utilisateur);
                    } else {
                        // Mode de sélection "Single" : Désélectionner tous les RadioButtons sauf celui cliqué
                        utilisateur.setSelected(false);
                    }
                } else {
                    // Aucun RadioButton sélectionné
                    selectAll = false;
                }

                if (utilisateur.getRadioButton().isSelected()) {
                    selectionMultiple = true;
                }
            }

            if (selectAll) {
                // Aucun RadioButton n'est sélectionné, donc sélectionner tous les RadioButtons
                for (Utilisateur utilisateur : utilisateurArrayList) {
                    utilisateur.setSelected(true);
                    utilisateursSelectionnes.add(utilisateur);
                }
            }
        }
    }

    @FXML
    void SupprimerDoublonUtilisateurAction(ActionEvent event) {
        // Créer un HashSet pour stocker les clés des groupes
        HashSet<String> groupeKeys = new HashSet<>();

        // Créer une liste pour stocker les groupes à supprimer
        ArrayList<Utilisateur> utilisateursSupprimes = new ArrayList<>();

        int doublonsSupprimes = 0; // Compteur pour les doublons supprimés

        // Parcourir la liste des groupes
        for (Utilisateur utilisateur : utilisateurArrayList) {
            // Construire une clé unique pour chaque groupe en utilisant les attributs nom, département et nombre_utilisateur
            String groupeKey =utilisateur.getNom() + utilisateur.getPrenom() + utilisateur.getTelephone() + utilisateur.getAdresseMail();

            if (groupeKeys.contains(groupeKey)) {
                // Groupe en double trouvé, l'ajouter à la liste des groupes à supprimer et augmenter le compteur de doublons
                utilisateursSupprimes.add(utilisateur);
                doublonsSupprimes++;
            } else {
                // Ajouter la clé à HashSet si le groupe n'est pas en double
                groupeKeys.add(groupeKey);
            }
        }

        if (doublonsSupprimes > 0) {
            // Supprimer les doublons de la liste groupeArrayList
            utilisateurArrayList.removeAll(utilisateursSupprimes);
            TableauUtilisateur.setItems(observableList());
            mettreAJourTotalEnregistrements();

            // Afficher un message à l'utilisateur indiquant le nombre de doublons supprimés
            String message = doublonsSupprimes + " doublon(s) ont été supprimés.";

            // Afficher le message dans une boîte de dialogue
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Doublons");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            // Aucun doublon à supprimer
            // Afficher un message à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Doublons");
            alert.setHeaderText(null);
            alert.setContentText("Aucun doublon à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void SupprimerUtilisateur(ActionEvent event) {
        // Créer une liste pour stocker les groupes sélectionnés
        ArrayList<Utilisateur> utilisateursSelectionnes = new ArrayList<>();

        // Parcourir la liste des groupes
        for (Utilisateur utilisateur : utilisateurArrayList) {
            // Vérifier si le groupe est sélectionné
            if (utilisateur.isSelected()) {
                // Ajouter le groupe à la liste des groupes sélectionnés
                utilisateursSelectionnes.add(utilisateur);
            }
        }

        // Supprimer les groupes sélectionnés de la liste groupeArrayList
        utilisateurArrayList.removeAll(utilisateursSelectionnes);

        // Mettre à jour le tableau en utilisant la méthode observableList()
        TableauUtilisateur.setItems(observableList());

        // Mettre à jour le total des enregistrements
        mettreAJourTotalEnregistrements();
    }

    @FXML
    void lienaccueilutilisateur(ActionEvent event) {
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 400);
            stage.setTitle("PierTable-APPLICATION");
            Scene sceneActuelle = ((Node) event.getSource()).getScene();
            Window fenetreActuelle = sceneActuelle.getWindow();
            ((Stage) fenetreActuelle).close();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void liendeconnexion(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(StartApplication.class.getResource("loginPage.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void lienpagecreationgroupe(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(StartApplication.class.getResource("pageCréationGroupe.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void lienpagecreationuser(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(StartApplication.class.getResource("pageCréationUtilisateur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mettreAJourTotalEnregistrements() {
        int totalEnregistrements = utilisateurArrayList.size();
        TotaldesEnregistrementutilisateur.setText("Total : " + totalEnregistrements);
    }
}