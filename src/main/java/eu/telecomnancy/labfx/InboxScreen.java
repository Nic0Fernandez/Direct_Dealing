package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class InboxScreen {
  private final Main main;
  private final User user;

  @FXML
  private ListView<String> conversationList;

  @FXML
  private TextField interlocutorField;

  @FXML
  private Conversation conversationViewController;

  public InboxScreen(Main main, User user) {
    this.main = main;
    this.user = user;
  }

  @FXML
  void initialize() {
    
  }

  @FXML
  private void selectConversation() {

  }

  @FXML
  private void nouvelleConversation() {
    
  }
}
