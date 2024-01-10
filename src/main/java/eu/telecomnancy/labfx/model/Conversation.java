package eu.telecomnancy.labfx.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Conversation {
  public Set<Integer> userIds;

  public ObservableList<Message> messages;

  public int id;

  public boolean userInConversation(int id) {
    return userIds.contains(id);
  }

  public ObservableList<Message> getMessages() {
    return messages;
  }

  public Set<Integer> getUserIds() {
    return userIds;
  }

  public Conversation(int id1, int id2) {
    userIds = new HashSet<>(2);
    userIds.add(id1);
    userIds.add(id2);
    assert userIds.size() == 2;
    messages = FXCollections.observableArrayList();
  }

  public Conversation() {
    userIds = new HashSet<>(2);
    messages = FXCollections.observableArrayList();
  }

  public void addMessage(int id, String message) {
    if (!userInConversation(id))
      return;
    messages.add(new Message(id, message));
  }

  public int getOtherUser(int id) {
    if (!userInConversation(id)) return -1;
    Optional<Integer> other = userIds.stream().filter(UID -> UID != id).findAny();
    if (!other.isPresent()) return -1;
    return other.get();
  }
}
