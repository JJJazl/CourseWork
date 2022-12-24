module com.example.coursework {
    requires org.postgresql.jdbc;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.coursework to javafx.fxml;
    exports com.example.coursework;
    exports com.example.coursework.dao;
    opens com.example.coursework.dao to javafx.fxml;
    exports com.example.coursework.domain;
    opens com.example.coursework.domain to javafx.fxml;
    exports com.example.coursework.controller;
    opens com.example.coursework.controller to javafx.fxml;
}