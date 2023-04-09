package com.example.Project_Table.BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String url;
    private final String username;
    private final String password;

    public Database(String host, int port, String dbName, String username, String password) {
        this.url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        // Remplacez ces valeurs par celles de votre NAS et de votre base de données MariaDB
        String host = "109.234.162.158:3306";
        int port = 3306;
        String dbName = "gocl4291_wegrow_sandbox";
        String username = "gocl4291_pcottin";
        String password = "sVP&d8h;$eYT";

        Database db = new Database(host, port, dbName, username, password);

        try (Connection conn = db.getConnection()) {
            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }

        /*
        * CREATE TABLE Ligne(
	idLigne int auto_increment primary key,
    ValeurLigne char,
    NumeroLigne int,
    FormuleLigne char
)

* CREATE TABLE dataTable(
	idColonne int auto_increment primary key,
    Nom char,
    Type char,
    Ligne_id int,
    foreign key (Ligne_id) REFERENCES Ligne(idLigne)
)
        * */
    }
}
