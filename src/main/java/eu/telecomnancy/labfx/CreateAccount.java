package eu.telecomnancy.labfx.login;

import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateAccount {
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

    public void clickButton() {
        user = new User("1");
        user.username = nom.getText() + " " + prenom.getText();
        user.password = motdepasse.getText();
        user.email = email.getText();
        user.address = adresse.getText();
        user.florains = 500;
        System.out.println(user.username);


    }
}
