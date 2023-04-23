module com.example.quality_software_rendu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.json;
    requires java.desktop;

    opens com.example.Project_Table to javafx.fxml;
    exports com.example.Project_Table;
    exports com.example.Project_Table.controller to javafx.fxml;
    opens com.example.Project_Table.controller to javafx.fxml;

}