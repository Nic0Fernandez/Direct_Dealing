package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Message;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class MessageCellFactory implements Callback<ListView<Message>, ListCell<Message>> {

  private final int userID;
  private final int otherID;

  public MessageCellFactory(int userID, int otherID) {
    this.userID = userID;
    this.otherID = otherID;
  }

  @Override
  public ListCell<Message> call(ListView<Message> param) {
    return new ListCell<>() {

      @Override
      public void updateItem(Message entry, boolean empty) {
        super.updateItem(entry, empty);
        if (empty || entry == null) {
          setText(null);
        } else {
          Label label = new Label(entry.text);
          HBox box = new HBox(label);
          if (entry.id == userID)
            setAlignment(Pos.CENTER_RIGHT);
          else if (entry.id == otherID)
            setAlignment(Pos.CENTER_LEFT);
          setGraphic(box);
        }
      }
    };
  }
}