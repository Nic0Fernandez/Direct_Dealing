package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Conversation;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.Message;
import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ConversationView {
  private final Main main;
  private final User user;
  private User interlocutor;

  @FXML
  private ListView<Message> conversationList;

  @FXML
  private Label userLabel;

  @FXML
  private TextArea messageField;

  @FXML
  private Button sendMessageButton;

  public ConversationView(Main main, User user) {
    this.main = main;
    this.user = user;
  }

  public void enable() {
    messageField.setDisable(false);
    sendMessageButton.setDisable(false);
    conversationList.setDisable(false);
  }

  public void setInterlocutor(User interlocutor) {
    this.interlocutor = interlocutor;
    userLabel.setText(interlocutor.username);
    enable();
    Conversation convo = JSONDatabase.getInstance().getConversation(user.UID, interlocutor.UID);
    conversationList.setCellFactory(new MessageCellFactory(user.UID, interlocutor.UID));
    conversationList.setItems(convo.getMessages());
  }

  private static void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
  }

  @FXML
  private void sendMessage() {
    String message = messageField.getText();
    if (message == null || message.isEmpty()) {
      showAlert("message vide", "pas permi d'envoyer une message vide!");
    }

    JSONDatabase.getInstance().sendMessage(user.UID, interlocutor.UID, message);
    messageField.clear();
  }
}
