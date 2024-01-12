package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.Database;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.Transaction;
import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DismissableNotification {
  private final Main main;
  private final Transaction t;
  private final User user;
  private final User other;
  private final Ad ad;

  Database db = JSONDatabase.getInstance();

  @FXML
  private Label notificationLabel;

  public DismissableNotification(Main main, Transaction t, User user) {
    this.main = main;
    this.t = t;
    this.user = user;
    ad = db.getAd(t.adID);
    if (ad.userID == user.UID) {
      other = db.getUser(t.UID);
    } else {
      other = db.getUser(ad.userID);
    }
  }

  @FXML
  void initialize() {
    String text = "";
    switch (t.statusType) {
      case ACCEPTED:
        text = other.username + " a accepté votre reservation pour \"" + ad.name + "\"";
        break;
      case COMPLETED:
        text = other.username + " a declaré le fin de \"" + ad.name + "\"";
        break;
      case REFUSED:
        text = other.username + " a refusé votre reservation pour \"" + ad.name + "\"";
        break;
      case RESERVED:
      text = other.username + " a demandé de reserver \"" + ad.name + "\"";
        break;
      default:
        break;
    }
    notificationLabel.setText(text);
  }

  @FXML
  private void seeOffer() throws IOException {
    main.viewOffer(user, ad);
  }

  @FXML
  private void sendMessage() throws IOException {
    main.inboxScreen(user, null, other);
  }

  @FXML
  private void dismissNotification() {
    db.removeNotification(user, t.ID);
  }

}
