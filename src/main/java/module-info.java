module com.san4os2008.grapheditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;

    opens com.san4os2008.grapheditor to javafx.fxml;
    exports com.san4os2008.grapheditor;
}