package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Conversation;
import eu.telecomnancy.labfx.model.Database;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ConversationCellFactory implements Callback<ListView<Integer>, ListCell<Integer>> {

  private static Database db = JSONDatabase.getInstance();

  private final User currentUser;

  public ConversationCellFactory(User currentUser) {
    this.currentUser = currentUser;
  }

  @Override
  public ListCell<Integer> call(ListView<Integer> param) {
    return new ListCell<>() {

      @Override
      public void updateItem(Integer entry, boolean empty) {
        super.updateItem(entry, empty);
        if (empty || entry == null) {
          setText(null);
        } else {
          Conversation convo = db.getConversation(entry);
          if (convo == null) {
            setText(null);
          }
          User other = db.getUser(convo.getOtherUser(currentUser.UID));
          setText(other.username + ": \"" + convo.getMessages().get(convo.getMessages().size() - 1).text + "\"");
        }
      }
    };
  }
}