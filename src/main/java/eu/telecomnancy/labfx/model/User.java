package eu.telecomnancy.labfx.model;

public class User {
  public final int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;

  public User(int UID) {
    this.UID = UID;
    sleepMode = false;
  }

}
