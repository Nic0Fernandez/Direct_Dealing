package eu.telecomnancy.labfx.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.annotation.JsonbTransient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JSONDatabase implements Database {

  private static JSONDatabase instance;

  static public JSONDatabase getInstance() {
    if (instance == null) {
      instance = new JSONDatabase();
    }
    return instance;
  }

  private final Map<Integer, User> idToUser = new HashMap<>();

  @JsonbTransient
  private final Map<String, User> usernameToUser = new HashMap<>();

  private final Map<Integer, Ad> ads = new HashMap<>();

  @JsonbTransient
  private final Random random = new Random();

  private int generateRandomPositiveInteger() {
    return random.nextInt() & Integer.MAX_VALUE;
  }

  // only for testing!!
  public JSONDatabase() {
  }

  private int getNewUserID() {
    int i;
    do {
      i = generateRandomPositiveInteger();

    } while (idToUser.containsKey(i));
    return i;
  }

  private int getNewAdID() {
    int i;
    do {
      i = generateRandomPositiveInteger();
    } while (ads.containsKey(i));
    return i;
  }

  @Override
  public int addUser(User user) {
    if (!isUsernameAvailable(user.username)) {
      return -1;
    }

    int uid = getNewUserID();
    user.UID = uid;
    idToUser.put(uid, user);
    usernameToUser.put(user.username, user);
    save();
    return uid;
  }

  @Override
  public int addAd(Ad ad) {
    if (ads.containsValue(ad)) {
      return ad.ID;
    }
    int id = getNewAdID();
    ad.ID = id;
    ads.put(id, ad);
    save();
    return id;
  }

  @Override
  public User getUser(int UID) {
    return idToUser.getOrDefault(UID, null);
  }

  @Override
  public Ad getAd(int ID) {
    return ads.getOrDefault(ID, null);
  }

  @Override
  public int authenticate(String username, String password) {
    if (isUsernameAvailable(username)) {
      return -1;
    }

    User user = usernameToUser.get(username);
    if (user.password != password) {
      return -1;
    }

    return user.UID;
  }

  @Override
  public ObservableList<Ad> getAdsAsList() {
    return FXCollections.observableArrayList(ads.values());
  }

  @Override
  public boolean isUsernameAvailable(String name) {
    return !usernameToUser.containsKey(name);
  }

  private void save() {

  }

  public String asJSON() {
    Jsonb jsonb = JsonbBuilder.create();

    return jsonb.toJson(this);
  }

}
