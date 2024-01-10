package eu.telecomnancy.labfx.model;

import java.util.HashSet;
import java.util.Set;

public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  public Set<Integer> conversations;

  public User() {
    sleepMode = false;
    conversations = new HashSet<>();
  }

}
