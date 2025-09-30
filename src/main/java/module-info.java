module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;
    requires java.desktop;
    requires java.xml.crypto;
    requires org.apache.pdfbox;
    requires javafx.swing;

    opens ku.cs.models to javafx.base;
    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;
}