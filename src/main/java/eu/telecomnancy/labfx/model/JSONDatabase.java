package eu.telecomnancy.labfx.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.IntSupplier;

import org.apache.commons.lang3.SystemUtils;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;

public class JSONDatabase implements Database {

  private static JSONDatabase instance;

  private static class RandomIdSupplier implements IntSupplier {
    private final Random random = new Random();

    @Override
    public int getAsInt() {
      return random.nextInt() & Integer.MAX_VALUE;
    }
  }

  static public Database getInstance() {
    if (instance == null) {
      instance = new JSONDatabase(new RandomIdSupplier(),
          System.getenv("HOME") + "/DirectDealing",
          System.getenv("APPDATA") + "\\DirectDealing");
      instance.load();
    }
    return instance;
  }

  @JsonbProperty("users")
  private final Map<Integer, User> idToUser = new HashMap<>();

  @JsonbTransient
  private final Map<String, User> usernameToUser = new HashMap<>();

  @JsonbProperty("ads")
  private final Map<Integer, Ad> ads = new HashMap<>();

  @JsonbTransient
  private final IntSupplier idProvider;

  // public only for testing!!
  public JSONDatabase(IntSupplier idProvider, String linuxPath, String windowsPath) {
    this.idProvider = idProvider;
    this.linuxPath = linuxPath;
    this.windowsPath = windowsPath;
  }

  private int getNewUserID() {
    int i;
    do {
      i = idProvider.getAsInt();

    } while (idToUser.containsKey(i));
    return i;
  }

  private int getNewAdID() {
    int i;
    do {
      i = idProvider.getAsInt();
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
      System.out.println("username \"" + username + "\" not found!");
      return -1;
    }

    User user = usernameToUser.get(username);
    if (!user.password.equals(password)) {
      System.out.println("wrong password for user " + username);
      return -1;
    }

    return user.UID;
  }

  @JsonbTransient
  @Override
  public ObservableList<Ad> getAdsAsList() {
    return FXCollections.observableArrayList(ads.values());
  }

  @Override
  public boolean isUsernameAvailable(String name) {
    return !(name == null || usernameToUser.containsKey(name));
  }

  private String windowsPath;
  private String linuxPath;

  private Path getPathToFile(String name) {
    File dir;
    if (SystemUtils.IS_OS_WINDOWS) {
      if (windowsPath == null)
        return null;
      dir = new File(windowsPath);
    } else {
      if (linuxPath == null)
        return null;
      dir = new File(linuxPath);
    }

    if (!dir.exists()) {
      System.out.println("attempting to create directory at " + dir.getAbsolutePath());
      if (!dir.mkdir()) {
        System.out.println("failed to create directory");
        return null;
      }
    }
    return Path.of(dir.getAbsolutePath(), name);
  }

  private void save() {

    Path path = getPathToFile("db.json");
    if (path == null)
      return;
    try {
      Files.write(path, asJSON().getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static class JSONDatabaseMemento {
    public Collection<Ad> ads;

    public Collection<Ad> getAds() {
      return ads;
    }

    public Collection<User> users;

    public Collection<User> getUsers() {
      return users;
    }

    public JSONDatabaseMemento() {
    }

    public JSONDatabaseMemento(Collection<Ad> ads, Collection<User> users) {
      this.ads = ads;
      this.users = users;
    }
  }

  private JSONDatabaseMemento toMemento() {
    return new JSONDatabaseMemento(ads.values(), idToUser.values());
  }

  public String asJSON() {
    Jsonb jsonb = JsonbBuilder.create();

    return jsonb.toJson(toMemento());
  }

  private void load() {
    Path dbFile = getPathToFile("db.json");
    if (dbFile == null || !Files.exists(dbFile)) {
      return;
    }

    try {
      String dbString = Files.readString(dbFile);
      loadFromJSON(dbString);
    } catch (IOException e) {
      System.out.println("programme n'a pas reussi à ouvrir le fichier à " + dbFile.toString());
    }
  }

  void loadFromJSON(String dbString) {
    try {
      Jsonb jsonb = JsonbBuilder.create();
      JSONDatabaseMemento m = jsonb.fromJson(dbString, JSONDatabaseMemento.class);
      loadFromMemento(m);
    } catch (JsonbException e) {
      System.out.println("programme n'a pas reussi à interpreter le fichier json: " + e.getMessage());
    }
  }

  private void loadFromMemento(JSONDatabaseMemento m) {
    m.ads.forEach(ad -> ads.put(ad.ID, ad));
    m.users.forEach(user -> {
      idToUser.put(user.UID, user);
      usernameToUser.put(user.username, user);
    });
  }

  @Override
  public String saveImage(String path) {

    try {
      BufferedImage image = ImageIO.read(new File(path));
      String copyPath = getPathToFile(Path.of(path).getFileName().toString()).toString();
      String extension = "";
      int i = path.lastIndexOf(".");
      if (i > 0) {
        extension = path.substring(i + 1);
      }
      try {
        ImageIO.write((RenderedImage) image, extension, new File(copyPath));
        return copyPath;
      } catch (Exception e) {
        System.out.println("Cannot write image");
        return null;
      }
    } catch (Exception e) {
      System.out.println("Cannot read image");
      return null;
    }
  }
}
