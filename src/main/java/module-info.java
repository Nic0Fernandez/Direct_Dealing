module eu.telecomnancy.labfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.json.bind;
    requires java.desktop;
    requires transitive javafx.graphics;


    opens eu.telecomnancy.labfx to javafx.fxml;
    exports eu.telecomnancy.labfx;
    exports eu.telecomnancy.labfx.model;
}
