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
  public Map<Integer,Integer> transactionsExt = new HashMap<>(); //les offres qu'on a réservées 
  public Map<Integer,Integer> transactionsIn = new HashMap<>(); //nos offres qui sont réservées
  
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

  public int createTransaction(Ad ad){
    Transaction transaction = new Transaction(ad,this.UID, StatusType.RESERVED);
    JSONDatabase.getInstance().addTransaction(transaction);
    System.out.println("Transaction ID: "+ transaction.ID);
    return transaction.ID;
  }
}


