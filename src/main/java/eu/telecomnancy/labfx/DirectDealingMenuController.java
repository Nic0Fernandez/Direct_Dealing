package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

public class DirectDealingMenuController {

  public enum Screen {
    MAIN,
    CREATE,
    INBOX,
    VIEW,
    PROFILE
  };

  private final Main main;
  private final User user;
  private final Screen currentScreen;

  @FXML
  private MenuItem mainScreenItem;

  @FXML
  private MenuItem profileScreenItem;

  @FXML
  private MenuItem inboxItem;

  @FXML
  private MenuItem createScreenItem;

  public DirectDealingMenuController(Main main, User user, Screen currentScreen) {
    this.main = main;
    this.user = user;
    this.currentScreen = currentScreen;
  }

  @FXML
  void initialize() {
    switch (currentScreen) {
      case CREATE:
        createScreenItem.setDisable(true);
        break;
      case INBOX:
        inboxItem.setDisable(true);
        break;
      case MAIN:
        mainScreenItem.setDisable(true);
        break;
      case PROFILE:
        profileScreenItem.setDisable(true);
        break;
      default:
        break;
    }
  }

  @FXML
  void showAbout() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("À propos de DirectDealing");
    alert.setHeaderText(null);
    alert
        .setContentText("TelecomNancy DirectDealing est une application d’économie circulaire en ligne permettant à\n" + //
            "des personnes de prêter/emprunter du matériel (tondeuse, marteau-piqueur, épluche-légumes,\n" + //
            "etc.) et/ou de proposer/demander des services (réparation d’une fuite d’eau, leçon de piano,\n" + //
            "déménagement, etc.)");
    alert.show();
  }

  @FXML
  void logout() throws IOException {
    main.loginScreen();
  }

  @FXML
  void closeApp() {
    Platform.exit();
  }

  @FXML
  void goToMainScreen() throws IOException {
    main.mainScreen(user);
  }

  @FXML
  void goToProfileScreen() throws IOException {
    main.ViewCompteController(user);
  }

  @FXML
  void goToInbox() throws IOException {
    main.inboxScreen(user, null, null);
  }

  @FXML
  void goToCreateScreen() throws IOException {
    main.viewCreateOffer(user);
  }

}
