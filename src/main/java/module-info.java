module org.example.emergency_connect {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.datatransfer;
    requires java.sql; // This is likely the one you need for clipboard, if any.

    opens org.example.emergency_connect to javafx.fxml;
    exports org.example.emergency_connect;
}
