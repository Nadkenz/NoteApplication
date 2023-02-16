module com.nadia.noteapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;


    opens com.nadia.noteapplication to javafx.fxml;
    exports com.nadia.noteapplication;
}