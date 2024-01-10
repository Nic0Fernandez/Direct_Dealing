package eu.telecomnancy.labfx.model;

import java.util.*;
public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  public String imgpath ;
  public List<AdHistory> history = new ArrayList();

  public User() {
    sleepMode = false;
  }

  public int getUID() {
    return UID;
  }

public String getUserName() {
    return username;
  }

public String getPhotoPath() {
    return imgpath;
  }

  public void setPhotoPath(String pathImage) {
    pathImage=this.imgpath ;
  }

  public void addToHistory(Ad ad){
    AdHistory newAd = new AdHistory(ad,StatusType.RESERVED);
    history.add(newAd);
  }
}


