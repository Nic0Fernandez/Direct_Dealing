package eu.telecomnancy.labfx.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Conversation {
  public Set<Integer> userIds;

  public List<Message> messages;

  public int id;

  public boolean userInConversation(int id) {
    return userIds.contains(id);
  }

  public List<Message> getMessages() {
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
    messages = new ArrayList<>();
  }

  public Conversation() {
    userIds = new HashSet<>(2);
    messages = new ArrayList<>();
  }

  public void addMessage(int id, String message) {
    if (!userInConversation(id))
      return;
    messages.add(new Message(id, message));
  }
}
