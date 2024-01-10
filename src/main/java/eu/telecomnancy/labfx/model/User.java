package eu.telecomnancy.labfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  public ObservableList<Integer> conversations;

  public User() {
    sleepMode = false;
    conversations = FXCollections.observableArrayList();
  }

}
