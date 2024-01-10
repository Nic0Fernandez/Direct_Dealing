package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

  @Test
  void testAsJSON() {
    for (int i = 0; i < 3; ++i) {
      Ad ad = new Ad();
      ad.description = "ad number " + i;
      db.addAd(ad);
      User user = new User();
      user.username = "user number " + i;
      db.addUser(user);
    }
    String expected = "{\"ads\":[{\"ID\":0,\"cost\":0,\"description\":\"ad number 0\",\"duration\":0,\"isOffer\":false,\"maxDistance\":0.0,\"userID\":0},{\"ID\":2,\"cost\":0,\"description\":\"ad number 1\",\"duration\":0,\"isOffer\":false,\"maxDistance\":0.0,\"userID\":0},{\"ID\":4,\"cost\":0,\"description\":\"ad number 2\",\"duration\":0,\"isOffer\":false,\"maxDistance\":0.0,\"userID\":0}],\"users\":[{\"UID\":1,\"conversations\":[],\"florains\":0,\"sleepMode\":false,\"username\":\"user number 0\"},{\"UID\":3,\"conversations\":[],\"florains\":0,\"sleepMode\":false,\"username\":\"user number 1\"},{\"UID\":5,\"conversations\":[],\"florains\":0,\"sleepMode\":false,\"username\":\"user number 2\"}]}";
    assertEquals(expected, db.asJSON());
  }

  @Test
  void testFromJSON() {
    String jsonString = "{\"ads\":[{\"ID\":0},\n" +
        "{\"ID\":2},\n" +
        "{\"ID\":4}],\n" +
        "\"users\":[{\"UID\":1,\"username\":\"user number 0\"},\n" +
        "{\"UID\":3,\"username\":\"user number 1\"},\n" +
        "{\"UID\":5,\"username\":\"user number 2\"}]}";
    db.loadFromJSON(jsonString);
    assertNotNull(db.getAd(0));
    assertNotNull(db.getAd(2));
    assertNotNull(db.getAd(4));
    assertNotNull(db.getUser(1));
    assertNotNull(db.getUser(3));
    assertNotNull(db.getUser(5));
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
}
