module eu.telecomnancy.labfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.json.bind;


    opens eu.telecomnancy.labfx to javafx.fxml;
    exports eu.telecomnancy.labfx;
    exports eu.telecomnancy.labfx.model;
}