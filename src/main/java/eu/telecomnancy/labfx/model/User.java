package eu.telecomnancy.labfx.model;

public class User {
  public final String UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  
  public User(String UID) {
    this.UID = UID;
    sleepMode = false;
  }
}
