package com.example.Project_Table.BDD;

import com.example.Project_Table.Modele.Groupe;
import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.Modele.Utilisateur;
import com.example.Project_Table.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {

    //region Variable
    private String url;
    private String username;
    private String password;
    private ArrayList<Table> listTable;

    private ObservableList<String> items;

    private Utilisateur utilisateur;

    private ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<>();
    private ArrayList<Groupe> lesGroupes = new ArrayList<>();
    //endregion

    public Database() {
        this.url = "jdbc:mariadb://109.234.162.158:3306/gocl4291_wegrow_sandbox";
        //this.url = "jdbc:mariadb://11.106.0.18:3306:3306/gocl4291_wegrow_sandbox";
        this.username = "gocl4291_pcottin";
        this.password = "*YF!b+BjsUey";
        this.listTable = new ArrayList<Table>();
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    public ArrayList<Table> getListTable() {
        return listTable;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public ArrayList<Utilisateur> getLesUtilisateurs() {
        return lesUtilisateurs;
    }

    public void setLesUtilisateurs(ArrayList<Utilisateur> lesUtilisateurs) {
        this.lesUtilisateurs = lesUtilisateurs;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setListTable(ArrayList<Table> listTable) {
        this.listTable = listTable;
    }

    public ArrayList<Groupe> getLesGroupes() {
        return lesGroupes;
    }

    public void setLesGroupes(ArrayList<Groupe> lesGroupes) {
        this.lesGroupes = lesGroupes;
    }

    public void addTable(Table table){
        this.listTable.add(table);
    }

    public Table getTable(String name){
        Table tableReturn = null;
        for (Table tableCourant : listTable){
            if(Objects.equals(name, tableCourant.getNom())){
                tableReturn = tableCourant;
            }
        }
        return tableReturn;
    }


    public Table fetchQuery(String adresseMailUser){
        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();

            String sql = "SELECT data.*, Editer,Viewer FROM dataTable data, ViewGroupe where nameTable = NomTable "+
                    "and adresseMailUser = '"+adresseMailUser+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String jsonString = rs.getString("jsonTable");
                Boolean Ecriture = rs.getBoolean("Editer");
                Boolean Lecteur = rs.getBoolean("Viewer");
                JSONObject jsonObject = new JSONObject(jsonString);
                String nom = rs.getString("nameTable");
                Table table = new Table(jsonObject,Ecriture,Lecteur);
                Boolean trouver = false;
                int i = 0;
                for(Table currentTable : listTable){
                    if(table.getNom().equals(currentTable.getNom())){
                        if(currentTable.getEcriture() && table.getEcriture()){
                            listTable.remove(i);
                            listTable.add(table);
                            trouver = true;
                            break;
                        } else if(!currentTable.getEcriture() && table.getEcriture()){
                            listTable.remove(i);
                            listTable.add(table);
                            trouver = true;
                            break;
                        } else if(currentTable.getEcriture() && !table.getEcriture()){

                        } else if(!currentTable.getEcriture() && !table.getEcriture()){
                            listTable.remove(i);
                            listTable.add(table);
                            trouver = true;
                            break;
                        }
                        trouver = true;
                    }
                    i++;
                }
                if(!trouver) {
                    listTable.add(table);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }

        return null;
    }

    public ObservableList<String> getItems(){
        return items;
    }

    public void createObservableList(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int j = 0; j < this.getListTable().size(); j++){
            arrayList.add(this.getListTable().get(j).getNom());
        }
        arrayList.add("+ Nouvelle table");
        items = FXCollections.observableArrayList(arrayList);
    }

    public void save(){
        Boolean saveOn = false;
        if(listTable.size() > 0) {
            try (Connection conn = this.getConnection()) {
                Statement stmt = conn.createStatement();

                String stringRequest = "";
                for (Table currentTable : listTable) {
                    stringRequest += "('" + currentTable.getNom() + "','" + currentTable.toJson().toString().replace("\\", "\\\\") + "',null,null,null),";
                }
                stringRequest = stringRequest.substring(0, stringRequest.length() - 1);
                String sql = "insert into dataTable (nameTable,jsonTable,ViewUserId,EditUserId,AdminUserId) values" + stringRequest + ";";
                stmt.executeQuery(sql);
                saveOn = true;
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
                saveOn = false;
            }
            if (saveOn) {
                try (Connection conn = this.getConnection()) {
                    Statement stmt = conn.createStatement();

                    String sql = "TRUNCATE TABLE dataTable";
                    stmt.executeQuery(sql);

                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
                }
            }
            try (Connection conn = this.getConnection()) {
                Statement stmt = conn.createStatement();

                String stringRequest = "";
                for (Table currentTable : listTable) {
                    stringRequest += "('" + currentTable.getNom() + "','" + currentTable.toJson().toString().replace("\\", "\\\\") + "',null,null,null),";
                }
                stringRequest = stringRequest.substring(0, stringRequest.length() - 1);
                String sql = "insert into dataTable (nameTable,jsonTable,ViewUserId,EditUserId,AdminUserId) values" + stringRequest + ";";
                stmt.executeQuery(sql);

                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        }
    }
    public boolean verificationAdresseMailMDPOublier(String adresseMail){
        Boolean result = false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(adresseMail.matches(emailRegex)){
            try (Connection conn = this.getConnection()) {
                String sql = "Select * from Utilisateur where adresseMail = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, adresseMail); // Modifier la valeur du ? par adresseMail.

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    long telephone = rs.getLong("telephone");
                    String motDePasse = rs.getString("motDePasse");
                    result = true;
                }

                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        }
        return result;
    }

    public boolean verificationUser(String adresseMail, String motdePasse){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!?+=])(?=\\S+$).{6,}$";
        Boolean result = false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(adresseMail.matches(emailRegex) && motdePasse.matches(passwordRegex)){
            try (Connection conn = this.getConnection()) {
                String sql = "Select * from Utilisateur where adresseMail = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, adresseMail); // Modifier la valeur du ? par adresseMail.

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String telephone = rs.getString("telephone");
                    String motDePasseData = rs.getString("motDePasse");
                    if(motdePasse.equals(motDePasseData)){
                        utilisateur = new Utilisateur(nom,prenom,adresseMail,telephone,motDePasseData);
                    }
                    result = true;
                }

                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        }
        return result;
    }

    public void addUtilisateur(Utilisateur unUtilisateur){
        lesUtilisateurs.add(unUtilisateur);
    }

    public void clearUtilisateur(){
        lesUtilisateurs.clear();
    }

    public void addAllUtilisateur(List<Utilisateur> desUtilisateurs){
        this.lesUtilisateurs = (ArrayList<Utilisateur>) desUtilisateurs;
    }

    public List<Utilisateur> fetchUtilisateur(){
        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();
            String sql = "select nom,prenom,adresseMail,telephone, NomGroupe,NomTable,Viewer, Editer\n" +
                    "from Utilisateur u,\n" +
                    "ViewGroupe vg\n" +
                    "where adresseMail != \"Admin.Admin@gmail.com\"\n" +
                    "and u.adresseMail = vg.adresseMailUser";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String adresseMail = rs.getString("adresseMail");
                String telephone = rs.getString("telephone");
                ArrayList<Groupe> tableauDeGroupe = new ArrayList<>();
                if(verificationAdresseMail(adresseMail)){
                    try (Connection connection = this.getConnection()) {
                        Statement statement = connection.createStatement();
                        String sqlRequest = "select adresseMailUser,NomGroupe,NomTable,Viewer, Editer\n" +
                                "from ViewGroupe\n" +
                                "where adresseMailUser = '"+adresseMail+"'\n";
                        ResultSet result = statement.executeQuery(sqlRequest);
                        while (result.next()) {
                            String adresseMailUser = result.getString("adresseMailUser");
                            String NomGroupe = result.getString("NomGroupe");
                            String NomTable = result.getString("NomTable");
                            Boolean viewer = result.getBoolean("Viewer");
                            if(verificationGroupe(NomGroupe)){
                                Groupe groupe = new Groupe(NomGroupe);
                                if(viewer){
                                    groupe.addPrivillege(adresseMailUser,"Lecteur");
                                } else{
                                    groupe.addPrivillege(adresseMailUser,"Editeur");
                                }
                                ArrayList<Table> listTable = new ArrayList<>();
                                listTable.add(getTable(NomTable));
                                groupe.setTable(listTable);
                                tableauDeGroupe.add(groupe);
                                lesGroupes.add(groupe);
                            } else {
                                Groupe leGroupe = getGroupe(NomGroupe);
                                leGroupe.addTable(getTable(NomTable));
                            }
                        }
                        result.close();
                        statement.close();

                    }catch (SQLException e) {
                        System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
                    }
                    Utilisateur unUtilisateur = new Utilisateur(nom,prenom,telephone,adresseMail,tableauDeGroupe);
                    lesUtilisateurs.add(unUtilisateur);
                }
            }
            rs.close();
            stmt.close();

        }catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }


        return lesUtilisateurs;
    }

    public Boolean verificationAdresseMail(String adresseMail){
        Boolean result = true;
        for(Utilisateur unUtilisateur : lesUtilisateurs){
            if(unUtilisateur.getAdresseMail().equals(adresseMail)){
                result = false;
            }
        }
        return result;
    }

    public Boolean verificationGroupe(String nomGroupe){
        Boolean result = true;
        for(Groupe unGroupe : lesGroupes){
            if(unGroupe.getNom().equals(nomGroupe)){
                result = false;
            }
        }
        return result;
    }

    public Groupe getGroupe(String nomGroupe){
        Groupe resultGroupe = null;
        for(Groupe unGroupe : lesGroupes){
            if(unGroupe.getNom().equals(nomGroupe))
                resultGroupe = unGroupe;
        }
        return resultGroupe;
    }
}

