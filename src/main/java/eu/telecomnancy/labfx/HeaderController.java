package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Message;
import eu.telecomnancy.labfx.model.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class HeaderController {
  private final Main main;
  private final User user;
  private boolean allowReturn;

  @FXML
  private Label currencyLabel;

  @FXML
  private Button returnButton;

  public HeaderController(Main main, User user, boolean allowReturn) {
    this.main = main;
    this.user = user;
  }

  @FXML
  private void initialize() {
    if (!allowReturn) {
      returnButton.setDisable(true);
      returnButton.setVisible(false);
    }
    currencyLabel.textProperty().bind(user.florains.asString());
  }

  @FXML
  private void onClickReturn() {

  }


}
