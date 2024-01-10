package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ConversationView {
  private final Main main;
  private final User user;
  private User interlocutor;
  @FXML
  private ListView<String> conversationList;

  public ConversationView(Main main, User user) {
    this.main = main;
    this.user = user;
  }

  @FXML
  void initialize() {

  }

  public void setInterlocutor(User interlocutor) {
    this.interlocutor = interlocutor;
  }

  @FXML
  private void selectConversation() {

  }
}
