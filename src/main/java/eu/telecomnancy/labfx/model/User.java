package eu.telecomnancy.labfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  public ObservableList<Integer> conversations;
  public String imgpath ;
  public List<AdHistory> history = new ArrayList();

  public User() {
    sleepMode = false;
    conversations = FXCollections.observableArrayList();
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


