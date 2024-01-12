package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.Database;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.StatusType;
import eu.telecomnancy.labfx.model.Transaction;
import eu.telecomnancy.labfx.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ReservationNotification {
  private final Main main;
  private final Transaction t;
  private final User reserver;
  private final User adPublisher;
  private final Ad ad;

  Database db = JSONDatabase.getInstance();

  @FXML
  private Label notificationLabel;

  public ReservationNotification(Main main, Transaction t) {
    this.main = main;
    this.t = t;
    reserver = db.getUser(t.UID);
    ad = db.getAd(t.adID);
    adPublisher = db.getUser(ad.userID);
  }

  @FXML
  void initialize() {
    String labelText = reserver.username + " a reservé votre offre " + ad.name + "!";
    notificationLabel.setText(labelText);
  }

  @FXML
  private void acceptReservation() {
    updateNotifications();
    db.saveStatus(t, StatusType.ACCEPTED);
  }

  @FXML
  private void refuseReservation() {
    updateNotifications();
    db.saveStatus(t, StatusType.REFUSED);
  }

  @FXML
  private void putOnWaitList() {
  }

  @FXML
  private void seeOffer() throws IOException {
    main.viewOffer(adPublisher, ad);
  }

  @FXML
  private void sendMessage() throws IOException {
    main.inboxScreen(adPublisher, null, reserver);
  }

  private void updateNotifications() {
    reserver.addNotification(t.ID);
    adPublisher.removeNotification(t.ID);
  }

}
