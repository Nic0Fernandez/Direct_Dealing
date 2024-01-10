package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.Conversation;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class InboxScreen {
  private final Main main;
  private final User user;
  private final Ad returnOffer;

  @FXML
  private ListView<Integer> conversationsList;

  @FXML
  private TextField interlocutorField;

  @FXML
  private TextArea messageField;

  @FXML
  private ConversationView conversationViewController;

  public InboxScreen(Main main, User user) {
    this.main = main;
    this.user = user;
    this.returnOffer = null;
  }

  public InboxScreen(Main main, User user, Ad returnOffer) {
    this.main = main;
    this.user = user;
    this.returnOffer = returnOffer;
  }

  @FXML
  void initialize() {
    conversationsList.setItems(user.conversations);
    conversationsList.setCellFactory(new ConversationCellFactory(user));
    if (returnOffer != null) {
      User interlocutor = JSONDatabase.getInstance().getUser(returnOffer.userID);
      interlocutorField.setText(interlocutor.username);
    }
  }

  @FXML
  private void selectConversation(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY) {
      Integer conversationID = conversationsList.getSelectionModel().getSelectedItem();
      if (conversationID == null)
        return;
      Conversation convo = JSONDatabase.getInstance().getConversation(conversationID);
      Integer id = convo.getOtherUser(user.UID);
      User interlocutor = JSONDatabase.getInstance().getUser(id);
      if (interlocutor == null)
        return;
      conversationViewController.setInterlocutor(interlocutor);
    }
  }

  private static void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
  }

  @FXML
  private void newConversation() {
    String otherUsername = interlocutorField.getText();
    User interlocutor = JSONDatabase.getInstance().getUser(otherUsername);
    if (interlocutor == null) {
      showAlert("utilisateur pas trouvé",
          "utilisateur avec nom d'utilisateur " + otherUsername + " pas trouvé");
    }
    if (interlocutor == user) {
      showAlert("Conversation avec soi même",
          "Pas permi d'envoyer une message à soi même!");
    }
    String message = messageField.getText();
    if (message == null || message.isEmpty()) {
      showAlert("message vide", "pas permi d'envoyer une message vide!");
    }
    JSONDatabase.getInstance().sendMessage(user.UID, interlocutor.UID, message);
    conversationViewController.setInterlocutor(interlocutor);
    messageField.clear();
  }

  @FXML
  void returnButton() throws IOException {
    if (returnOffer != null) {
      main.viewOffer(user, returnOffer);
    } else {
      main.mainScreen(user);
    }
  }
}
