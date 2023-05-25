package com.example.Project_Table;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class ControleurGroupe implements Initializable {

    @FXML
    private AnchorPane PageCreationGroupe;

    @FXML
    private Hyperlink pageCreationutilisateurlien;

    @FXML
    private Hyperlink pageCreationGroupelien;

    @FXML
    private Hyperlink pageListeutilisateurslien;

    @FXML
    private Label nomidentifiantadminOut;

    @FXML
    private Hyperlink LienPageDeconnexion;

    @FXML
    private Hyperlink LienPageAccueil;

    @FXML
    private Button SauvegardeGroupeCreerBouton;

    @FXML
    private TextArea NomGroupeIn;

    @FXML
    private TextArea DepartementGroupeIn;

    @FXML
    private TableView<Groupe> TableauduGroupe;

    @FXML
    private TableColumn<Groupe, String> ColonneTableauNomGroupe;

    @FXML
    private RadioButton ColonneSelectduTableauGroupe;

    @FXML
    private TableColumn<Groupe, String> ColonneDepartementduTableauGroupe;

    @FXML
    private TableColumn<Groupe, Integer> ColonneNombreUtilisateurduTableauGroupe;

    @FXML
    private TableColumn<Groupe, RadioButton> colonneSelectgroupe;


    @FXML
    private Pagination DefilerPageGroupeIn;


    @FXML
    private Button SupprimerGroupedanslalisteBouton;

    @FXML
    private Button ModifierGroupedanslalisteBouton;


    @FXML
    private Button SupprimerdoublonsGroupedanslalisteBouton1;

    @FXML
    private ComboBox<String> comboBoxGroupes;

    @FXML
    private Label TotaldesEnregistrement;

    private int elementsParPage = 3; // Remplacez par le nombre d'éléments souhaité par page
    private ObservableList<Groupe> groupeObservableList; // on creer une liste observable de groupe qui nous permettra de pouvoir voir tous les modifications de groupe et de pouvoir etre utilisé par l'ensembles des fonctions
    ArrayList<Groupe> groupesSelectionnes = new ArrayList<>(); // Elle permet de stocker et de manipuler des object groupe(add, delete).
    private String adresseEmailUtilisateur; // Variable pour stocker l'adresse e-mail de l'utilisateur


    // je fait juste cette fonction pour pouvoir changer le label nom utilisateur par l'email de l'utilisateur.
    public void setAdresseEmailUtilisateur(String adresseEmailUtilisateur) {
        this.adresseEmailUtilisateur = adresseEmailUtilisateur;
    }

    // pour enreistrer les informations d'un groupe
    @FXML
    public void EnregistrerGroupeAction(ActionEvent actionEvent) {
        // je recupère les informations entrer par l'admin pour la création du groupe
        String nomgroupe = NomGroupeIn.getText();
        String departementgroupe = DepartementGroupeIn.getText();

        //je verifit si les informations sont valides
        boolean nomgroupeValide = verificationnomgroupe(nomgroupe);
        boolean departementgroupeValide = verificationdepartementgroupe(departementgroupe);

        if (nomgroupeValide && departementgroupeValide) {
            // Si les informations sont valides,    on remplit le tableau
            // Création d'un nouvel objet Groupe avec les informations entrées par l'admin
            Groupe nouveaugroupe = new Groupe(nomgroupe, departementgroupe, 0);
            groupeArrayList.add(nouveaugroupe);// ajouter dans le stockage le nomgroupe et departementgroupe et utilisateur
            TableauduGroupe.setItems(observableList()); // permet de mettre à jour le contenu du tableau avec les nouvelles données

            // Permet de verifier si le nombre d'éléments dans la groupeObservableList atteint ou dépasse la
            // limite elementsParPage. Si tel est le cas, cela signifie que la liste a atteint la capacité maximale d'une page.
            if (groupeObservableList.size() >= elementsParPage) {
                int currentPageIndex = DefilerPageGroupeIn.getCurrentPageIndex();
                int nextPageIndex = currentPageIndex + 1;
                if (nextPageIndex < DefilerPageGroupeIn.getPageCount()) {
                    // Passer à la page suivante
                    DefilerPageGroupeIn.setCurrentPageIndex(nextPageIndex);
                }
            }

            // Mettre à jour le tableau avec les nouveaux enregistrements
            updateTable(DefilerPageGroupeIn.getCurrentPageIndex());

            // Mettre à jour le total des enregistrements
            mettreAJourTotalEnregistrements();
            // Réinitialisation des champs de saisie
            reinitialiser();

        } else {
            // Affichage d'un message d'erreur
            // Afficher un message d'erreur si les infos du groupe ne sont pas valides
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informations invalides");
            alert.setHeaderText(null);
            alert.setContentText("Le nom et le département doivent être des chaines de caractères de maximun 50 caractères.");
            alert.showAndWait();
        }
    }

    // Vérifie si les informations sont valides
    boolean verificationnomgroupe(String nomgroupe) {
        boolean isValid = nomgroupe != null && !nomgroupe.isEmpty() && nomgroupe.length() <= 50;
        return isValid;
    }

    // Vérifie si les informations sont valides
    boolean verificationdepartementgroupe(String departementgroupe) {
        boolean isValid = departementgroupe != null && !departementgroupe.isEmpty() && departementgroupe.length() <= 50;
        return isValid;
    }

    ArrayList<Groupe> groupeArrayList = new ArrayList<>();
    ObservableList<Groupe> groupetab;

    public void reinitialiser() {

        NomGroupeIn.setText("");
        DepartementGroupeIn.setText("");
    }
    public ObservableList<Groupe> observableList() {
        //pour radio bouton
        groupetab = FXCollections.observableArrayList(groupe -> new Observable[]{groupe.selectedProperty()});
        groupetab.addAll(groupeArrayList);
        return groupetab;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColonneTableauNomGroupe.setCellValueFactory(new PropertyValueFactory<Groupe, String>("Nom"));
        ColonneDepartementduTableauGroupe.setCellValueFactory(new PropertyValueFactory<Groupe, String>("Departement"));
        ColonneNombreUtilisateurduTableauGroupe.setCellValueFactory(new PropertyValueFactory<Groupe, Integer>("Nombre_utilisateur"));

        TableauduGroupe.setItems(observableList());
        // pour le radiobouton
        colonneSelectgroupe.setCellValueFactory(cellData -> {
            Groupe groupe = cellData.getValue();

            RadioButton radioButton = new RadioButton();
            radioButton.setSelected(groupe.isSelected());
            radioButton.selectedProperty().bindBidirectional(groupe.selectedProperty());

            return new SimpleObjectProperty<>(radioButton);
        });

        // Calculer le nombre total de pages
        int totalEnregistrements = groupeArrayList.size();
        int totalPages = (int) Math.ceil((double) totalEnregistrements / elementsParPage);

        // Limiter le nombre de pages affichées dans la barre de défilement
        DefilerPageGroupeIn.setPageCount(totalPages);

        // Initialiser le tableau avec le nombre initial d'éléments par page
        updateTable(0);

        // Réagir aux changements de page
        DefilerPageGroupeIn.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateTable(newIndex.intValue());
        });

        // Mettre à jour le texte du Label avec l'adresse e-mail de l'utilisateur
        nomidentifiantadminOut.setText(adresseEmailUtilisateur);

        // Mettre à jour le total des enregistrements
        mettreAJourTotalEnregistrements();
    }
    public void updateTable(int pageIndex) {
        int startIndex = pageIndex * elementsParPage;
        int endIndex = Math.min(startIndex + elementsParPage, groupeArrayList.size());
        List<Groupe> elementsAffiches = groupeArrayList.subList(startIndex, endIndex);
        groupeObservableList = FXCollections.observableArrayList(elementsAffiches);
        TableauduGroupe.setItems(groupeObservableList);
    }

    @FXML
    void ModifierGroupe(ActionEvent event) {
        // Vérifier si un groupe a été sélectionné
        Groupe groupeSelectionne = null;
        for (Groupe groupe : groupeArrayList) {
            if (groupe.isSelected()) {
                if (groupeSelectionne == null) {
                    groupeSelectionne = groupe;
                } else {
                    // Plusieurs groupes sélectionnés, afficher un message à l'utilisateur
                    String message = "Veuillez sélectionner uniquement un groupe à modifier.";
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

        // Vérifier si un groupe a été sélectionné
        if (groupeSelectionne != null) {
            // Les informations du groupe sélectionné sont disponibles
            // Afficher les informations dans les champs appropriés pour modification
            NomGroupeIn.setText(groupeSelectionne.getNom());
            DepartementGroupeIn.setText(groupeSelectionne.getDepartement());

            // Supprimer le groupe sélectionné de la liste groupeArrayList
            groupeArrayList.remove(groupeSelectionne);

            // Mettre à jour le total des enregistrements
            mettreAJourTotalEnregistrements();
        }
    }


    @FXML
    void SupprimerGroupe(ActionEvent event) {
        // Créer une liste pour stocker les groupes sélectionnés
        ArrayList<Groupe> groupesSelectionnes = new ArrayList<>();

        // Parcourir la liste des groupes
        for (Groupe groupe : groupeArrayList) {
            // Vérifier si le groupe est sélectionné
            if (groupe.isSelected()) {
                // Ajouter le groupe à la liste des groupes sélectionnés
                groupesSelectionnes.add(groupe);
            }
        }

        // Supprimer les groupes sélectionnés de la liste groupeArrayList
        groupeArrayList.removeAll(groupesSelectionnes);

        // Mettre à jour le tableau en utilisant la méthode observableList()
        TableauduGroupe.setItems(observableList());

        // Mettre à jour le total des enregistrements
        mettreAJourTotalEnregistrements();
    }

    @FXML
    void lienaccueilutilisateur(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void liendeconnexion(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void lienlisteutilisateur(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pageListeUtilisateur.fxml")));
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pageCréationUtilisateur.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour générer le contenu du tableau au format CSV
    private String generateCSVContent() {
        StringBuilder csvContent = new StringBuilder();
        // Ajouter les en-têtes des colonnes
        csvContent.append("Nom, Département, Nombre_utilisateur\n");
        // Parcourir la liste des groupes et ajouter les données au contenu CSV
        for (Groupe groupe : groupeArrayList) {
            csvContent.append(groupe.getNom()).append(",");
            csvContent.append(groupe.getDepartement()).append(",");
            csvContent.append(groupe.getNombre_utilisateur()).append("\n");
        }
        return csvContent.toString();
    }

    // Méthode pour exporter le contenu du tableau au format CSV
    @FXML
    void ExporterFicherGroupeAction(ActionEvent event)  {
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

    private List<Groupe> obtenirTableauGroupes() {
        // Retournez le contenu actuel du tableau groupetab ou de toute autre source de données
        return new ArrayList<>(groupetab);
    }

    @FXML
    void SupprimerDoublonGroupe(ActionEvent event) {
        // Créer un HashSet pour stocker les clés des groupes
        HashSet<String> groupeKeys = new HashSet<>();

        // Créer une liste pour stocker les groupes à supprimer
        ArrayList<Groupe> groupesSupprimes = new ArrayList<>();

        int doublonsSupprimes = 0; // Compteur pour les doublons supprimés

        // Parcourir la liste des groupes
        for (Groupe groupe : groupeArrayList) {
            // Construire une clé unique pour chaque groupe en utilisant les attributs nom, département et nombre_utilisateur
            String groupeKey = groupe.getNom() + groupe.getDepartement() + groupe.getNombre_utilisateur();

            if (groupeKeys.contains(groupeKey)) {
                // Groupe en double trouvé, l'ajouter à la liste des groupes à supprimer et augmenter le compteur de doublons
                groupesSupprimes.add(groupe);
                doublonsSupprimes++;
            } else {
                // Ajouter la clé à HashSet si le groupe n'est pas en double
                groupeKeys.add(groupeKey);
            }
        }

        if (doublonsSupprimes > 0) {
            // Supprimer les doublons de la liste groupeArrayList
            groupeArrayList.removeAll(groupesSupprimes);
            TableauduGroupe.setItems(observableList());
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
    void SelectionnerToutAction(ActionEvent event){

        if (ColonneSelectduTableauGroupe.isSelected()) {
            // Mode de sélection "All" : Sélectionner tous les RadioButton de la colonne colonneSelectgroupe
            for (Groupe groupe : groupeArrayList) {
                groupe.setSelected(true);
                groupesSelectionnes.add(groupe);
            }
        } else {
            boolean selectionMultiple = false;
            boolean selectAll = true;

            for (Groupe groupe : groupeArrayList) {
                if (groupe.getRadioButton().isSelected()) {
                    // Au moins un RadioButton est sélectionné
                    selectAll = false;

                    if (selectionMultiple) {
                        // Mode de sélection "Multiple" : Garder la sélection actuelle des RadioButtons
                        groupe.setSelected(groupe.getRadioButton().isSelected());
                        groupesSelectionnes.add(groupe);
                    } else {
                        // Mode de sélection "Single" : Désélectionner tous les RadioButtons sauf celui cliqué
                        groupe.setSelected(false);
                    }
                } else {
                    // Aucun RadioButton sélectionné
                    selectAll = false;
                }

                if (groupe.getRadioButton().isSelected()) {
                    selectionMultiple = true;
                }
            }

            if (selectAll) {
                // Aucun RadioButton n'est sélectionné, donc sélectionner tous les RadioButtons
                for (Groupe groupe : groupeArrayList) {
                    groupe.setSelected(true);
                    groupesSelectionnes.add(groupe);
                }
            }
        }
    }


    private void mettreAJourTotalEnregistrements() {
        int totalEnregistrements = groupeArrayList.size();
        TotaldesEnregistrement.setText("Total : " + totalEnregistrements);
    }


    @FXML
    void ImporterFicherGroupeAction(ActionEvent event) {
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
                List<Groupe> groupesImportes = new ArrayList<>();

                // Parcourir les lignes du fichier CSV
                for (String line : lines) {
                    // Diviser la ligne en colonnes en utilisant la virgule comme séparateur
                    String[] columns = line.split(",");

                    if (columns.length >= 2) {
                        String nomGroupe = columns[0].trim();
                        String departementGroupe = columns[1].trim();

                        // Vérifier si les informations du groupe sont valides
                        boolean nomGroupeValide = verificationnomgroupe(nomGroupe);
                        boolean departementGroupeValide = verificationdepartementgroupe(departementGroupe);

                        if (nomGroupeValide && departementGroupeValide) {
                            // Créer un nouvel objet Groupe avec les informations du fichier CSV
                            Groupe groupe = new Groupe(nomGroupe, departementGroupe, 0);
                            groupesImportes.add(groupe);
                        }
                    }
                }

                if (groupesImportes.isEmpty()) {
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
                        groupeArrayList.addAll(groupesImportes);
                    } else if (result.get() == replaceButton) {
                        // Remplacer la liste existante par les groupes importés
                        groupeArrayList.clear();
                        groupeArrayList.addAll(groupesImportes);
                    }
                }

                // Mettre à jour le tableau avec les nouveaux enregistrements
                updateTable(DefilerPageGroupeIn.getCurrentPageIndex());

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


    @FXML
    void RechercheTrieAction(ActionEvent event) {
        //pas fait
    }
}

