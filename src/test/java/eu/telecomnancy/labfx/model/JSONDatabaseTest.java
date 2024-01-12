package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.IntSupplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;

public class JSONDatabaseTest {

  JSONDatabase db;

  private static class LinearIDSupplier implements IntSupplier {
    private int i = 0;

    @Override
    public int getAsInt() {
      return i++;
    }

  }

  @BeforeEach
  void setUp() {
    db = new JSONDatabase(new LinearIDSupplier(), null, null);
    JSONDatabase.instance = db;
  }

  @Test
  void testAddAd() {
    Ad ad = new Ad();
    int id = db.addAd(ad);
    assertNotEquals(-1, id);
    assertEquals(ad, db.getAd(id));
  }

  @Test
  void testAddAlreadyPresent() {
    Ad ad = new Ad();
    int id1 = db.addAd(ad);
    assertNotEquals(-1, id1);
    assertEquals(ad, db.getAd(id1));
    assertEquals(id1, db.addAd(ad));
  }

  @Test
  void testAddUser() {
    User user = new User();
    user.username = "Harry";
    int id = db.addUser(user);
    assertNotEquals(-1, id);
    assertEquals(user.UID, id);
    assertEquals(user, db.getUser(id));
  }

  @Test
  void testAddUserAlreadyPresent() {
    User user = new User();
    user.username = "Harry";
    int id = db.addUser(user);
    assertNotEquals(-1, id);
    assertEquals(user.UID, id);
    assertEquals(user, db.getUser(id));
    assertEquals(-1, db.addUser(user));
  }

  @Test
  void testAddUserSameUsername() {
    User user = new User();
    user.username = "Harry";
    db.addUser(user);
    User user2 = new User();
    user2.username = "Harry";
    assertEquals(-1, db.addUser(user2));
  }

  String jsonDBString = "{\"ads\":[{\"ID\":1,\"cost\":0,\"description\":\"ad number 0\",\"duration\":0,\"maxDistance\":0.0,\"offer\":false,\"userID\":0},{\"ID\":3,\"cost\":0,\"description\":\"ad number 1\",\"duration\":0,\"maxDistance\":0.0,\"offer\":false,\"userID\":2},{\"ID\":5,\"cost\":0,\"description\":\"ad number 2\",\"duration\":0,\"maxDistance\":0.0,\"offer\":false,\"userID\":4}],\"conversations\":[{\"id\":6,\"messages\":[{\"id\":0,\"text\":\"a\"},{\"id\":2,\"text\":\"b\"}],\"userIds\":[0,2]},{\"id\":7,\"messages\":[{\"id\":4,\"text\":\"c\"}],\"userIds\":[4,2]}],\"transactions\":[{\"ID\":8,\"UID\":4,\"adID\":3,\"statusType\":\"RESERVED\"}],\"users\":[{\"UID\":0,\"conversations\":[6],\"florains\":0,\"pendingNotifications\":[],\"sleepMode\":false,\"transactionsExt\":{},\"transactionsIn\":{},\"username\":\"user number 0\"},{\"UID\":2,\"conversations\":[6,7],\"florains\":0,\"pendingNotifications\":[8],\"sleepMode\":false,\"transactionsExt\":{},\"transactionsIn\":{\"3\":8},\"username\":\"user number 1\"},{\"UID\":4,\"conversations\":[7],\"florains\":0,\"pendingNotifications\":[],\"sleepMode\":false,\"transactionsExt\":{\"3\":8},\"transactionsIn\":{},\"username\":\"user number 2\"}]}";

  @Test
  void testAsJSON() {
    User[] users = new User[3];
    Ad[] ads = new Ad[3];
    for (int i = 0; i < 3; ++i) {
      users[i] = new User();
      users[i].username = "user number " + i;
      db.addUser(users[i]);
      ads[i] = new Ad();
      ads[i].userID = users[i].UID;
      ads[i].description = "ad number " + i;
      db.addAd(ads[i]);
    }
    db.sendMessage(0, 2, "a");
    db.sendMessage(2, 0, "b");
    db.sendMessage(4, 2, "c");
    users[2].createTransaction(ads[1]);

    assertEquals(jsonDBString, db.asJSON());
  }

  @Test
  void testFromJSON() {
    db.loadFromJSON(jsonDBString);
    assertNotNull(db.getAd(1));
    assertNotNull(db.getAd(3));
    assertNotNull(db.getAd(5));
    assertNotNull(db.getUser(0));
    assertNotNull(db.getUser(2));
    assertNotNull(db.getUser(4));
    assertNotNull(db.getConversation(6));
    assertNotNull(db.getConversation(7));
    assertNotNull(db.getTransaction(8));
  }

  @Test
  void testAuthenticateNoUser() {
    assertEquals(-1, db.authenticate("doesn't", "matter"));
  }

  @Test
  void testAuthenticateWrongPassword() {
    User user = new User();
    user.username = "user";
    user.password = "0000";
    assertNotEquals(-1, db.addUser(user));
    assertEquals(-1, db.authenticate("user", "1234"));
  }

  @Test
  void testAuthenticateCorrect() {
    User user = new User();
    user.username = "user";
    user.password = "0000";
    int id = db.addUser(user);
    assertNotEquals(-1, id);
    assertEquals(id, db.authenticate("user", "0000"));
  }

  @Test
  void testGetAdPresent() {
    Ad ad = new Ad();
    ad.description = "ad";
    int id = db.addAd(ad);
    assertEquals(ad, db.getAd(id));
  }

  @Test
  void testGetAdMissing() {
    assertNull(db.getAd(10));
  }

  @Test
  void testGetAdsAsList() {
    for (int i = 0; i < 3; ++i) {
      Ad ad = new Ad();
      ad.description = String.valueOf(i);
      db.addAd(ad);
    }
    ObservableList<Ad> list = db.getAdsAsList();
    assertNotNull(list);
    assertEquals(3, list.size());
  }

  @Test
  void testGetUserPresent() {
    User user = new User();
    user.username = "user";
    int id = db.addUser(user);
    assertEquals(user, db.getUser(id));
  }

  @Test
  void testGetUserMissing() {
    assertNull(db.getUser(10));
  }

  @Test
  void testIsUsernameAvailableTrue() {
    User user = new User();
    user.username = "name";
    db.addUser(user);
    assertTrue(db.isUsernameAvailable("Jefferson"));
  }

  @Test
  void testIsUsernameAvailableFalse() {
    User user = new User();
    user.username = "name";
    db.addUser(user);
    assertFalse(db.isUsernameAvailable("name"));
  }

  @Test
  void testSendMessageUserMissing() {
    User user = new User();
    user.username = "pedro";
    db.addUser(user);
    assertEquals(-1, db.sendMessage(0, 10, "hello!"));
    assertEquals(0, user.conversations.size());
  }

  @Test
  void testSendMessageNoPreviousConversation() {
    User user1 = new User();
    user1.username = "pedro";
    int id1 = db.addUser(user1);
    User user2 = new User();
    user2.username = "joao";
    int id2 = db.addUser(user2);

    int cID = db.sendMessage(0, 1, "hello!");
    assertNotEquals(-1, cID);
    assertEquals(cID, (int) user1.conversations.toArray()[0]);
    assertEquals(cID, (int) user2.conversations.toArray()[0]);
    Conversation conversation = db.getConversation(cID);
    assertNotNull(conversation);
    assertEquals(cID, conversation.id);
    assertTrue(conversation.userInConversation(id1));
    assertTrue(conversation.userInConversation(id2));
    assertEquals(1, conversation.messages.size());
    Message message = conversation.messages.get(0);
    assertEquals(0, message.id);
    assertEquals("hello!", message.text);
  }

  @Test
  void testSendMessagePreviousConversation() {
    User user1 = new User();
    user1.username = "pedro";
    int id1 = db.addUser(user1);
    User user2 = new User();
    user2.username = "joao";
    int id2 = db.addUser(user2);

    assertNotEquals(-1, db.sendMessage(0, 1, "hello!"));
    int cID = db.sendMessage(1, 0, "world!");

    assertNotEquals(-1, cID);
    assertEquals(cID, (int) user1.conversations.toArray()[0]);
    assertEquals(cID, (int) user2.conversations.toArray()[0]);
    Conversation conversation = db.getConversation(cID);
    assertNotNull(conversation);
    assertEquals(2, conversation.messages.size());
    Message message = conversation.messages.get(1);
    assertEquals(1, message.id);
    assertEquals("world!", message.text);
  }

  @Test
  void testTransactions() {
    User user1 = new User();
    user1.username = "pedro";
    int id1 = db.addUser(user1);

    User user2 = new User();
    user2.username = "joao";
    int id2 = db.addUser(user2);

    Ad ad = new Ad();
    ad.userID = id2;
    db.addAd(ad);

    int TID = user1.createTransaction(ad);
    assertEquals(1, user1.transactionsExt.size());
    assertEquals(0, user1.transactionsIn.size());
    assertEquals(0, user2.transactionsExt.size());
    assertEquals(1, user2.transactionsIn.size());
    assertEquals(TID, user1.transactionsExt.get(ad.ID));
    assertEquals(TID, user2.transactionsIn.get(ad.ID));
    Transaction t = db.getTransaction(TID);
    assertNotNull(t);
    assertEquals(user1.UID, t.UID);
    assertEquals(ad.ID, t.adID);
    db.saveStatus(t, StatusType.REFUSED);
    assertEquals(StatusType.REFUSED, t.statusType);
  }

  @Test
  void testGetTransactionNoTransaction() {
    assertNull(db.getTransaction(10));
  }
}
