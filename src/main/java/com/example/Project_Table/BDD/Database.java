package com.example.Project_Table.BDD;

import com.example.Project_Table.Modele.Table;
import com.example.Project_Table.StartApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private String url;
    private String username;
    private String password;
    private ArrayList<Table> listTable;

    private ObservableList<String> items;

    public Database() {
        this.url = "jdbc:mariadb://109.234.162.158:3306:3306/gocl4291_wegrow_sandbox";
        this.username = "gocl4291_pcottin";
        this.password = "sVP&d8h;$eYT";
        this.listTable = new ArrayList<Table>();
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    public ArrayList<Table> getListTable() {
        return listTable;
    }
    public void setListTable(ArrayList<Table> listTable) {
        this.listTable = listTable;
    }

    public void addTable(Table table){
        this.listTable.add(table);
    }

    public Table getTable(String name){
        Table tableReturn = null;
        for (Table tableCourant : listTable){
            System.out.println(tableCourant.getNom());
            if(Objects.equals(name, tableCourant.getNom())){
                tableReturn = tableCourant;
            }
        }
        return tableReturn;
    }

    public Table fetchQuery(){
        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM dataTable";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String jsonString = rs.getString("jsonTable");
                System.out.println(jsonString);
                JSONObject jsonObject = new JSONObject(jsonString);
                String nom = rs.getString("nameTable");
                Table table = new Table(jsonObject);
                listTable.add(table);
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
        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();

            String sql = "TRUNCATE TABLE dataTable";
            stmt.executeQuery(sql);

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
        try (Connection conn = this.getConnection()) {
            Statement stmt = conn.createStatement();

            String stringRequest = "";
            for(Table currentTable : listTable){
                stringRequest += "('"+currentTable.getNom()+"','"+currentTable.toJson()+"',null,null,null),";
            }
            stringRequest = stringRequest.substring(0,stringRequest.length() -1);
            String sql = "insert into dataTable (nameTable,jsonTable,ViewUserId,EditUserId,AdminUserId) values" + stringRequest+";";
            stmt.executeQuery(sql);

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}

