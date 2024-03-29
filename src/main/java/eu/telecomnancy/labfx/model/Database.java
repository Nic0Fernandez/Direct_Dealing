package eu.telecomnancy.labfx.model;

import javafx.collections.ObservableList;

public interface Database {
  boolean isUsernameAvailable(String name);

  int addUser(User user);

  int addAd(Ad ad);

  User getUser(int UID);

  User getUser(String username);

  Ad getAd(int ID);

  int authenticate(String username, String password);

  ObservableList<Ad> getAdsAsList();

  String saveImage(String path);

  int sendMessage(int from, int to, String text);

  Conversation getConversation(int conversationID);

  Conversation getConversation(int UID1, int UID2);

  int addTransaction(Transaction transaction);

  Transaction getTransaction(int ID);

  void saveStatus(Transaction transaction, StatusType statusType);

  void addNotification(User u, Integer tid);

  void removeNotification(User u, Integer tid);
}
