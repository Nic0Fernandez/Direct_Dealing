module eu.telecomnancy.labfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens eu.telecomnancy.labfx to javafx.fxml;
    exports eu.telecomnancy.labfx;
}