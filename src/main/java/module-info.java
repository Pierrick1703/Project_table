module com.example.quality_software_rendu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.Project_Table to javafx.fxml;
    exports com.example.Project_Table;
}