module com.example.database {
    requires javafx.controls;
    requires javafx.fxml;
    requires io; // iText kernel and layout dependencies
    requires kernel;
    requires layout;
    requires java.desktop;
    requires java.sql;
	requires javafx.graphics;

    // Add other necessary dependencies as required
    opens com.example.database to javafx.fxml;
    opens ClassesOfTables to javafx.base;
    exports com.example.database;
}
