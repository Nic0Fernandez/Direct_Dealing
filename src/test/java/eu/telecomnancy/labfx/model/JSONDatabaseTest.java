package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import javafx.collections.ObservableList;

public class JSONDatabaseTest {

  JSONDatabase db;

  @BeforeEach
  void setUp() {
    db = new JSONDatabase();
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
    String expected = "{\"ads\":[{\"ID\":1079697560,\"cost\":0,\"description\":\"ad number 0\",\"isOffer\":false,\"maxDistance\":0.0,\"offer\":false},{\"ID\":697695232,\"cost\":0,\"description\":\"ad number 1\",\"isOffer\":false,\"maxDistance\":0.0,\"offer\":false},{\"ID\":1103006306,\"cost\":0,\"description\":\"ad number 2\",\"isOffer\":false,\"maxDistance\":0.0,\"offer\":false}],\"users\":[{\"UID\":601182261,\"florains\":0,\"sleepMode\":false,\"username\":\"user number 2\"},{\"UID\":1592540795,\"florains\":0,\"sleepMode\":false,\"username\":\"user number 0\"},{\"UID\":1769761061,\"florains\":0,\"sleepMode\":false,\"username\":\"user number 1\"}]}";
    assertEquals(expected, db.asJSON());

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
