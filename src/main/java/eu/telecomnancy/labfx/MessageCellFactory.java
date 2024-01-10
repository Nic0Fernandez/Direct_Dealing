package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
          HBox box = new HBox();
          Region r = new Region();
          HBox.setHgrow(r, Priority.ALWAYS);
          if (entry.id == userID)
            box.getChildren().addAll(r, label);
          else if (entry.id == otherID)
            box.getChildren().addAll(label, r);
          setGraphic(box);
        }
      }
    };
  }
}