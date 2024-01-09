package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class Conversation {
  private final Main main;
  private final User user;
  private User interlocutor;
  @FXML
  private ListView<String> conversationList;


  public Conversation(Main main, User user) {
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
