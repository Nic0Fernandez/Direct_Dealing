module eu.telecomnancy.labfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens eu.telecomnancy.labfx.login to javafx.fxml;
    exports eu.telecomnancy.labfx.login;
}