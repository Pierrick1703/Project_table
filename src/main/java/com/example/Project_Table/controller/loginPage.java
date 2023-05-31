package com.example.Project_Table.controller;

import com.example.Project_Table.BDD.Database;
import com.example.Project_Table.StartApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;

public class loginPage {

    @FXML
    private VBox PageConnexion;

    @FXML
    private TextField IdentifiantUtilisateurIn;

    @FXML
    private Button ButtonValiderConnexion;

    @FXML
    private Hyperlink LienMotdepasseOublié;

    @FXML
    private TextArea MessageErreurConnexionOut;

    @FXML
    private PasswordField MotdepasseUtilisateurIn;

    private Database data = StartApplication.data;


    //Partie pour le bouton valider
    @FXML
    void ConnexionButtonAction(ActionEvent event) throws IOException {
        // je recupère les informations entrer par l'utilisateur
        String identifiant = IdentifiantUtilisateurIn.getText();
        String motdepasse = MotdepasseUtilisateurIn.getText();

      //je verifit les information d'idendification
        boolean verificationAuthentification = data.verificationUser(identifiant,motdepasse);

        if (verificationAuthentification) {
            if (data.getUtilisateur().getNom().equals("Admin")) {
                // Si les identifiants sont valides, on charge la page creationutilisateur-view.fxml
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("pageListeUtilisateur.fxml"));
                    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
                    Scene sceneActuelle = ((Node) event.getSource()).getScene();
                    Window fenetreActuelle = sceneActuelle.getWindow();
                    ((Stage) fenetreActuelle).close();
                    stage.setTitle("New Table");
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                data.fetchQuery(data.getUtilisateur().getAdresseMail());
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("excel.fxml"));
                    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
                    Scene sceneActuelle = ((Node) event.getSource()).getScene();
                    Window fenetreActuelle = sceneActuelle.getWindow();
                    ((Stage) fenetreActuelle).close();
                    stage.setTitle("New Table");
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.setOnHidden(event2 -> {
                        StartApplication.data.save();
                    });
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            // Sinon, on affiche un message d'erreur
            MessageErreurConnexionOut.setText("Identifiants incorrects.");
        }
    }
    //partie pour le mot de passe oublier
    @FXML
    void MotdepasseoubliéAction(ActionEvent event) {
        // Demander à l'utilisateur d'entrer son adresse mail
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mot de passe oublié");
        dialog.setHeaderText("Veuillez entrer votre adresse mail pour recevoir le lien de réinitialisation du mot de passe.");
        dialog.setContentText("Adresse mail:");

        // Obtenir la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();

        // Si l'utilisateur a entré une adresse mail, envoyer le lien de réinitialisation du mot de passe
        if (result.isPresent()) {
            String email = result.get();

            // Vérifier si l'adresse mail est valide
            boolean isEmailValid = StartApplication.data.verificationAdresseMailMDPOublier(email);

            if (isEmailValid) {
                // Envoyer le lien de réinitialisation du mot de passe à l'adresse mail
                // Code pour envoyer le lien par email
                // ...

                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Mot de passe oublié");
                alert.setHeaderText(null);
                alert.setContentText("Un lien de réinitialisation du mot de passe a été envoyé à votre adresse mail.");
                alert.showAndWait();
            } else {
                // Afficher un message d'erreur si l'adresse mail est invalide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adresse mail invalide");
                alert.setHeaderText(null);
                alert.setContentText("L'adresse mail entrée n'est pas valide.");
                alert.showAndWait();
            }
        }
    }

}


