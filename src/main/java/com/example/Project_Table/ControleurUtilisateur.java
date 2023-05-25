package com.example.Project_Table;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ControleurUtilisateur {

    @FXML
    private AnchorPane PageCreationUtilisateur;

    @FXML
    private Label nomidentifiantadminOut;

    @FXML
    private Hyperlink LienPageDeconnexion;

    @FXML
    private Hyperlink pageCreationGroupelien;

    @FXML
    private Hyperlink pageListeutilisateurslien;

    @FXML
    private Hyperlink LienPageAccueil;

    @FXML
    private Button SauvegardeUtilisateurCreerBouton;

    @FXML
    private TextArea NomUilisateurIn;

    @FXML
    private TextArea PrenomUilisateurIn;

    @FXML
    private TextArea EmailUilisateurIn;

    @FXML
    private TextArea TelUilisateurIn;

    @FXML
    private TextArea MotdepassepardefautUilisateurIn;

    @FXML
    private TextArea MatriculeUilisateurIn;

    @FXML
    private TextArea Groupeutilisateurin1;

    @FXML
    private TextArea Groupeutilisateurin2;

    @FXML
    private TextArea Groupeutilisateurin3;

    @FXML
    private TextArea Typeutilisateurgroupe1in;

    @FXML
    private TextArea Typeutilisateurgroupe3in;

    @FXML
    private TextArea Typeutilisateurgroupe2in;



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
    // Vérifie Matricule
    boolean verificationmatricule(String matricule) {
        boolean isValid = matricule != null && !matricule.isEmpty() && matricule.length() <= 50;
        return isValid;
    }
    // Vérifie groupe
    boolean verificationGroupe(String nometdepartementgroupe) {
       boolean isValid = nometdepartementgroupe.equals("nom/departement");
       return isValid;
    }
    boolean verificationprivillege(String privilege) {
        boolean isValid = privilege.equals("Super Admin") || privilege.equals("Admin") || privilege.equals("Utilisateur") || privilege.equals("Visiteur");
        return isValid;
    }

    public void reinitialiserutilisateur() {
        NomUilisateurIn.setText("");
        PrenomUilisateurIn.setText("");
        EmailUilisateurIn.setText("");
        TelUilisateurIn.setText("");
        MotdepassepardefautUilisateurIn.setText("");
        MatriculeUilisateurIn.setText("");
        Groupeutilisateurin1.setText("");
        Groupeutilisateurin2.setText("");
        Groupeutilisateurin3.setText("");
        Typeutilisateurgroupe1in.setText("");
        Typeutilisateurgroupe3in.setText("");
        Typeutilisateurgroupe2in.setText("");

    }

    @FXML
    void Enregistretutilisateur(ActionEvent event) {
        // Récupérer les valeurs des champs de saisie
        String nom = NomUilisateurIn.getText();
        String prenom = PrenomUilisateurIn.getText();
        String email = EmailUilisateurIn.getText();
        String telephone = TelUilisateurIn.getText();
        String motdepasse = MotdepassepardefautUilisateurIn.getText();
        String matricule = MatriculeUilisateurIn.getText();
        String groupe1 = Groupeutilisateurin1.getText();
        String groupe2 = Groupeutilisateurin2.getText();
        String groupe3 = Groupeutilisateurin3.getText();
        String privilege1 = Typeutilisateurgroupe1in.getText();
        String privilege2 = Typeutilisateurgroupe2in.getText();
        String privilege3 = Typeutilisateurgroupe3in.getText();

        // Vérifier la validité des champs obligatoires
        boolean isValidNom = verificationNometPrenomUtilisateur(nom);
        boolean isValidPrenom = verificationNometPrenomUtilisateur(prenom);
        boolean isValidEmail = verificationadressemail(email);
        boolean isValidTelephone = verificationTelephone(telephone);
        boolean isValidMotdepasse = verificationmotdepasse(motdepasse);
        boolean isValidMatricule = verificationmatricule(matricule);

        // Vérifier si les champs obligatoires sont tous valides
        if (isValidNom && isValidPrenom && isValidEmail && isValidTelephone && isValidMotdepasse && isValidMatricule) {
            boolean isValidGroupe = true; // Initialisation à true si le groupe n'est pas renseigné
            boolean isValidPrivilege = true; // Initialisation à true si le privilège n'est pas renseigné

            // Vérifier la validité du groupe s'il est renseigné
            if (!groupe1.isEmpty() || !groupe2.isEmpty() || !groupe3.isEmpty()) {
                isValidGroupe = verificationGroupe(groupe1) || verificationGroupe(groupe2) || verificationGroupe(groupe3);
            }

            // Vérifier la validité du privilège s'il est renseigné
            if (!privilege1.isEmpty() || !privilege2.isEmpty() || !privilege3.isEmpty()) {
                isValidPrivilege = verificationprivillege(privilege1) || verificationprivillege(privilege2) || verificationprivillege(privilege3);
            }

            if (isValidGroupe && isValidPrivilege) {
                Utilisateur utilisateur = new Utilisateur(nom, prenom, email, telephone, motdepasse, matricule);

                // Vérifier la sélection du groupe 1 et du privilège correspondant
                if (!groupe1.isEmpty()) {
                    if (privilege1.isEmpty()) {
                        // Afficher un message d'erreur pour le privilège manquant
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Informations du groupe invalides");
                        alert.setHeaderText(null);
                        alert.setContentText("Le privilège du groupe 1 est obligatoire. Veuillez le renseigner.");
                        alert.showAndWait();
                        return;
                    }
                    utilisateur.setPrivilege1(privilege1);
                }

                // Vérifier la sélection du groupe 2 et du privilège correspondant
                if (!groupe2.isEmpty()) {
                    if (privilege2.isEmpty()) {
                        // Afficher un message d'erreur pour le privilège manquant
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Informations du groupe invalides");
                        alert.setHeaderText(null);
                        alert.setContentText("Le privilège du groupe 2 est obligatoire. Veuillez le renseigner.");
                        alert.showAndWait();
                        return;
                    }
                    utilisateur.setPrivilege2(privilege2);
                }

                // Vérifier la sélection du groupe 3 et du privilège correspondant
                if (!groupe3.isEmpty()) {
                    if (privilege3.isEmpty()) {
                        // Afficher un message d'erreur pour le privilège manquant
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Informations du groupe invalides");
                        alert.setHeaderText(null);
                        alert.setContentText("Le privilège du groupe 3 est obligatoire. Veuillez le renseigner.");
                        alert.showAndWait();
                        return;
                    }
                    utilisateur.setPrivilege3(privilege3);
                }

                // Effectuer les opérations supplémentaires avec l'utilisateur et le groupe

                String messagesuccess = "L'utilisateur " + nom + " a été créé avec succès.";
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informations création utilisateur");
                alert.setHeaderText(null);
                alert.setContentText(messagesuccess);
                alert.showAndWait();
                // Enregistrer les informations dans le fichier CSV
                enregistrerDansFichiertxt(utilisateur);
            } else {
                // Afficher un message d'erreur pour les infos du groupe ou du privilège invalides
                String errorMessage = "";
                if (!isValidGroupe) {
                    errorMessage += "Les informations du groupe sont invalides. Veuillez vérifier le format (nom/département).\n";
                }
                if (!isValidPrivilege) {
                    errorMessage += "Les informations du privilège sont invalides.\n le privillége peux être soit (Utilisateur, Visiteur, Admin, Super Admin.\n bien repecter le format.\n";
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Informations invalides");
                alert.setHeaderText(null);
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
            // Réinitialisation des champs de saisie
            reinitialiserutilisateur();
        } else {
            // Afficher un message d'erreur pour les champs obligatoires invalides
            String messageErreur = "Les champs suivants sont invalides : ";
            if (!isValidNom) {
                messageErreur += "Nom (doit contenir 50 caractères max), ";
            }
            if (!isValidPrenom) {
                messageErreur += "Prénom (doit contenir 50 caractères max), ";
            }
            if (!isValidEmail) {
                messageErreur += "Email, ";
            }
            if (!isValidTelephone) {
                messageErreur += "Téléphone (doit être composé de 10 chiffres commençant par 06 ou 07), ";
            }
            if (!isValidMotdepasse) {
                messageErreur += "Mot de passe, ";
            }
            if (!isValidMatricule) {
                messageErreur += "Matricule (doit contenir 50 caractères max), ";
            }
            messageErreur = messageErreur.substring(0, messageErreur.length() - 2); // Supprimer la virgule et l'espace à la fin
            // Afficher un message d'erreur pour les champs obligatoires invalides
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informations invalides");
            alert.setHeaderText(null);
            alert.setContentText(messageErreur);
            alert.showAndWait();
        }
    }

    private void enregistrerDansFichiertxt(Utilisateur utilisateur) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fichier.txt", true))) {
            // Écriture des informations de l'utilisateur dans le fichier
            writer.write("Nom: " + utilisateur.getNom());
            writer.newLine();
            writer.write("Prénom: " + utilisateur.getPrenom());
            writer.newLine();
            writer.write("Email: " + utilisateur.getAdresseMail());
            writer.newLine();
            writer.write("Téléphone: " + utilisateur.getTélephone());
            writer.newLine();
            writer.write("Mot de passe: " + utilisateur.getMotdepasse());
            writer.newLine();
            writer.write("Matricule: " + utilisateur.getMatricule());
            writer.newLine();
            writer.write("groupe1: " + utilisateur.getGroupe1());
            writer.newLine();
            writer.write("prillege1: " + utilisateur.getPrivilege1());
            writer.newLine();
            writer.write("groupe2: " + utilisateur.getGroupe2());
            writer.newLine();
            writer.write("prillege2: " + utilisateur.getPrivilege2());
            writer.newLine();
            writer.write("groupe3: " + utilisateur.getGroupe3());
            writer.newLine();
            writer.write("prillege3: " + utilisateur.getPrivilege3());
            writer.newLine();
            writer.newLine(); // Ligne vide entre chaque utilisateur
            //message de success
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informations Enregistrées dans le fichier txt");
            alert.setHeaderText(null);
            alert.setContentText("sucess");
            alert.showAndWait();
        } catch (IOException e) {
            String messageenregistrement="Erreur lors de l'enregistrement dans le fichier : " + e.getMessage();
            //message de success
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informations Enregistrées dans le fichier txt");
            alert.setHeaderText(null);
            alert.setContentText(messageenregistrement);
            alert.showAndWait();
        }
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
    void lienpagecreationgroupe(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pageCréationGroupe.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
