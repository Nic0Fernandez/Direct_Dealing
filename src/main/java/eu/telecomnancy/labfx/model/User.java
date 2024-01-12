package eu.telecomnancy.labfx.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import jakarta.json.bind.annotation.JsonbProperty;

public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  @JsonbProperty
  public SimpleIntegerProperty florains;
  public boolean sleepMode;
  public ObservableList<Integer> conversations;
  public String imgpath;
  public Map<Integer, Integer> transactionsExt = new HashMap<>(); // les offres qu'on a réservées
  public Map<Integer, Integer> transactionsIn = new HashMap<>(); // nos offres qui sont réservées

  public ObservableList<Integer> pendingNotifications;

  public User() {
    sleepMode = false;
    conversations = FXCollections.observableArrayList();
    pendingNotifications = FXCollections.observableArrayList();
    florains = new SimpleIntegerProperty();
  }

  public int createTransaction(Ad ad) {
    Transaction transaction = new Transaction(ad, this.UID, StatusType.RESERVED);
    JSONDatabase.getInstance().addTransaction(transaction);
    return transaction.ID;
  }

  public int getFlorains() {
    return florains.get();
  }

  public void setFlorains(int florains) {
    this.florains.set(florains);
  }


}
