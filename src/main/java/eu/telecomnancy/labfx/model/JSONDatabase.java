package eu.telecomnancy.labfx.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;

import org.apache.commons.lang3.SystemUtils;

import eu.telecomnancy.labfx.ViewCompteController;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;

public class JSONDatabase implements Database {

  // only public for tests!!
  public static JSONDatabase instance;

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

  private final Map<Integer, User> idToUser = new HashMap<>();

  private final Map<String, User> usernameToUser = new HashMap<>();

  private final Map<Integer, Ad> ads = new HashMap<>();

  private final Map<Integer, Conversation> conversations = new HashMap<>();

  private final IntSupplier idProvider;

  private final Map<Integer, Transaction> transactions = new HashMap<>();

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

  private int getUniqueID(Set<Integer> definedIDS) {
    int i;
    do {
      i = idProvider.getAsInt();
    } while (definedIDS.contains(i));
    return i;
  }

  private int getNewAdID() {
    return getUniqueID(ads.keySet());
  }

  private int getNewTransactionID() {
    return getUniqueID(transactions.keySet());
  }

  private int getNewConversationID() {
    return getUniqueID(conversations.keySet());
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
  public int addTransaction(Transaction transaction) {
    if (transactions.containsValue(transaction)) {
      return transaction.ID;
    }
    int id = getNewTransactionID();
    transaction.ID = id;
    transactions.put(id, transaction);
    Ad ad = getAd(transaction.adID);
    User user = getUser(transaction.UID);
    User otherUser = getUser(ad.userID);
    addNotification(otherUser, transaction.ID);
    user.transactionsExt.put(ad.ID, transaction.ID);
    otherUser.transactionsIn.put(ad.ID, transaction.ID);
    save();
    return id;
  }

  @Override
  public User getUser(int UID) {
    return idToUser.getOrDefault(UID, null);
  }

  @Override
  public User getUser(String username) {
    return usernameToUser.getOrDefault(username, null);
  }

  @Override
  public Ad getAd(int ID) {
    return ads.getOrDefault(ID, null);
  }

  @Override
  public Transaction getTransaction(int ID) {
    return transactions.getOrDefault(ID, null);
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
    // try {
    // Files.write(path, asJSON().getBytes());
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

  }

  public static class JSONDatabaseMemento {
    public Collection<Ad> ads;

    public Collection<User> users;

    public Collection<Conversation> conversations;

    public Collection<Transaction> transactions;

    public Collection<Ad> getAds() {
      return ads;
    }

    public Collection<Conversation> getConversations() {
      return conversations;
    }

    public Collection<User> getUsers() {
      return users;
    }

    public Collection<Transaction> getTransactions() {
      return transactions;
    }

    public JSONDatabaseMemento() {
    }

    public JSONDatabaseMemento(Collection<Ad> ads, Collection<User> users, Collection<Conversation> conversations,
        Collection<Transaction> transactions) {
      this.ads = ads;
      this.users = users;
      this.conversations = conversations;
      this.transactions = transactions;
    }
  }

  private JSONDatabaseMemento toMemento() {
    return new JSONDatabaseMemento(ads.values(), idToUser.values(), conversations.values(), transactions.values());
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
      JsonbConfig config = new JsonbConfig().withDeserializers(new MessageListDesserializer(),
          new IntegerObservableListDesserializer());
      Jsonb jsonb = JsonbBuilder.create(config);
      JSONDatabaseMemento m = jsonb.fromJson(dbString, JSONDatabaseMemento.class);
      loadFromMemento(m);
    } catch (JsonbException e) {
      System.out.println("programme n'a pas reussi à interpreter le fichier json: " + e.getMessage());
    }
  }

  private void loadFromMemento(JSONDatabaseMemento m) {
    if (m.ads != null)
      m.ads.forEach(ad -> ads.put(ad.ID, ad));
    if (m.users != null)
      m.users.forEach(user -> {
        idToUser.put(user.UID, user);
        usernameToUser.put(user.username, user);
      });
    if (m.conversations != null)
      m.conversations.forEach(convo -> conversations.put(convo.id, convo));
    if (m.transactions != null)
      m.transactions.forEach(t -> transactions.put(t.ID, t));
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

  @Override
  public Conversation getConversation(int conversationID) {
    return conversations.getOrDefault(conversationID, null);
  }

  @Override
  public int sendMessage(int from, int to, String text) {
    if (!idToUser.containsKey(from) || !idToUser.containsKey(to))
      return -1;
    Optional<Conversation> conversation = conversations.values().stream()
        .filter(conv -> conv.userInConversation(from) && conv.userInConversation(to)).findAny();
    Conversation convo;
    if (conversation.isPresent()) {
      convo = conversation.get();
      convo.addMessage(from, text);
    } else {
      convo = new Conversation(from, to);
      convo.id = getNewConversationID();
      User sender = idToUser.get(from);
      User receiver = idToUser.get(to);
      sender.conversations.add(convo.id);
      receiver.conversations.add(convo.id);
      convo.addMessage(from, text);
      conversations.put(convo.id, convo);
    }
    save();
    return convo.id;
  }

  @Override
  public void saveStatus(Transaction transaction, StatusType statusType) {
    if (transaction.statusType != StatusType.COMPLETED && statusType == StatusType.COMPLETED) {
      transferFunds(transaction);
    }
    transaction.statusType = statusType;
    save();
    System.out.println("mise à jour");
  }

  public void transferFunds(Transaction t) {
    User payer;
    User receiver;
    Ad ad = getAd(t.adID);
    if (ad.offer) {
      payer = getUser(t.UID);
      receiver = getUser(ad.userID);
    } else {
      payer = getUser(ad.userID);
      receiver = getUser(t.UID);
    }
    payer.florains -= ad.cost;
    receiver.florains += ad.cost;
  }

  @Override
  public Conversation getConversation(int UID1, int UID2) {
    Optional<Conversation> convo = conversations.values().stream()
        .filter(c -> c.userInConversation(UID2) && c.userInConversation(UID1)).findAny();

    if (!convo.isPresent())
      return null;
    return convo.get();
  }

  @Override
  public void addNotification(User u, Integer tid) {
    if (!u.pendingNotifications.contains(tid)) {
      u.pendingNotifications.add(tid);
      save();
    }

  }

  @Override
  public void removeNotification(User u, Integer tid) {
    System.out.println(u.pendingNotifications.size());
    if (u.pendingNotifications.remove(Integer.valueOf(tid))) {
      save();
    }

  }
}
