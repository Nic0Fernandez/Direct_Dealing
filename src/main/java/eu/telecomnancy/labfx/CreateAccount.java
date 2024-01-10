package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.User;
import eu.telecomnancy.labfx.model.JSONDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccount {

    public Main main;

    public User user;

    @FXML 
    TextField nom;

    @FXML
    TextField prenom;

    @FXML
    PasswordField motdepasse;

    @FXML
    TextField email;

    @FXML
    TextField adresse;

    public CreateAccount(Main main) {
        this.main = main;
    }

    public void clickButton() throws IOException {
        if(nom.getText().isBlank() || prenom.getText().isBlank() || motdepasse.getText().isBlank() || email.getText().isBlank() || adresse.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Fields without information", "Please fill all the fields");
            return;
        } 
        else if(JSONDatabase.getInstance().isUsernameAvailable(nom.getText() + " " + prenom.getText())) {
            addAccount();
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Username already exists!", "Please enter a new username");
            return;
        }

    }

    public void addAccount() throws IOException {
    user = new User();
    user.username = nom.getText() + " " + prenom.getText();
    user.password = motdepasse.getText();
    user.email = email.getText();
    user.address = adresse.getText();
    user.florains = 500;

    int result = JSONDatabase.getInstance().addUser(user);

    System.out.println("Add Account Result: " + result); // Add this line for debugging

    if (result != -1) {
        main.mainScreen(user);
    } else {
        showAlert(Alert.AlertType.ERROR, "Error creating account", "Please try again later");
    }
}


    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void backLogin() throws IOException {
        
        main.loginScreen();
    }
}
