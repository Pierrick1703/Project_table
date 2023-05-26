package com.example.Project_Table;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HelloController {

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




    //Partie pour le bouton valider
    @FXML
    void ConnexionButtonAction(ActionEvent event) {
        // je recupère les informations entrer par l'utilisateur
        String identifiant = IdentifiantUtilisateurIn.getText();
        String motdepasse = MotdepasseUtilisateurIn.getText();

      //je verifit les information d'idendification
        boolean identifiantsValides = verificationadressemail(identifiant);
        boolean motDePasseValide = verificationmotdepasse(motdepasse);

        if (identifiantsValides && motDePasseValide) {
            // Si les identifiants sont valides,    on charge la page creationutilisateur-view.fxml
            try{
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("creationutilisateur-view.fxml")));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
               } catch (IOException e) {
                    e.printStackTrace();
               }
        } else {
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
            boolean isEmailValid = verificationadressemail(email);

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

    // Vérifie si l'adresse mail est valide
    boolean verificationadressemail(String identifiant){
        // On utilise une expression régulière pour valider l'adresse mail
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValid = identifiant.matches(emailRegex);
        return isValid;
    }

    // Vérifie si le mot de passe est valide
    boolean verificationmotdepasse(String motdepasse){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        boolean isValid = motdepasse.matches(passwordRegex);
        return isValid;
    }

}


