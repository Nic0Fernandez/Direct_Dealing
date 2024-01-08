package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.User;
import eu.telecomnancy.labfx.model.JSONDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CreateAccount {

    public Main main;

    public User user;

    @FXML 
    TextField nom;

    @FXML
    TextField prenom;

    @FXML
    TextField motdepasse;

    @FXML
    TextField email;

    @FXML
    TextField adresse;

    public CreateAccount(Main main) {
        this.main = main;
    }

    public void clickButton() throws IOException {
        if(JSONDatabase.getInstance().isUsernameAvailable(nom.getText() + " " + prenom.getText())) {
            addAccount();
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Username already exists!", "Please enter a new username");
            return;
        }


    }

    public void addAccount() throws IOException{
        user = new User();
        user.username = nom.getText() + " " + prenom.getText();
        user.password = motdepasse.getText();
        user.email = email.getText();
        user.address = adresse.getText();
        user.florains = 500;
        JSONDatabase.getInstance().addUser(user);
        main.mainScreen(user);

    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
