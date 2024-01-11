package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public class UserTest {

  @Test
  void serializeTest() {
    User user = new User();
    user.UID = 1;
    user.username = "Lucas Skora";
    user.password = "password";
    user.address = "193 Av. Paul Muller, 54600 Villers-lès-Nancy";
    user.email = "e2455u@telecomnancy.net";
    user.florains = 10;
    user.conversations.add(1);
    user.transactionsExt.put(1, 2);
    user.transactionsExt.put(2, 1);
    user.transactionsIn.put(5, 6);
    user.pendingNotifications.addAll(1, 2, 3);

    Jsonb jsonb = JsonbBuilder.create();
    String jsonUser = jsonb.toJson(user);

    String expected = "{\"UID\":1,\"address\":\"193 Av. Paul Muller, 54600 Villers-lès-Nancy\",\"conversations\":[1],\"email\":\"e2455u@telecomnancy.net\",\"florains\":10,\"password\":\"password\",\"pendingNotifications\":[1,2,3],\"sleepMode\":false,\"transactionsExt\":{\"1\":2,\"2\":1},\"transactionsIn\":{\"5\":6},\"username\":\"Lucas Skora\"}";

    assertEquals(expected, jsonUser);
  }

  @Test
  void deserializeTest() {
    JsonbConfig config = new JsonbConfig().withDeserializers(new IntegerObservableListDesserializer());
    Jsonb jsonb = JsonbBuilder.create(config);
    String jsonString = "{\"UID\":10,\"conversations\":[1,2,3],\"address\":\"c\",\n" +
        "\"email\":\"d\",\"florains\":500,\"password\":\"b\",\"sleepMode\":false,\"username\":\"a\",\n" +
        "\"transactionsExt\":{\"1\":2},\"transactionsIn\":{\"5\":6},\"pendingNotifications\":[1]}";
    User expectedUser = new User();
    expectedUser.UID = 10;
    expectedUser.username = "a";
    expectedUser.password = "b";
    expectedUser.address = "c";
    expectedUser.email = "d";
    expectedUser.florains = 500;
    expectedUser.sleepMode = false;
    expectedUser.conversations.add(1);
    expectedUser.conversations.add(2);
    expectedUser.conversations.add(3);
    expectedUser.transactionsExt.put(1, 2);
    expectedUser.transactionsIn.put(5, 6);
    expectedUser.pendingNotifications.add(1);

    User user = jsonb.fromJson(jsonString, User.class);

    assertEquals(expectedUser.UID, user.UID);
    assertEquals(expectedUser.username, user.username);
    assertEquals(expectedUser.password, user.password);
    assertEquals(expectedUser.address, user.address);
    assertEquals(expectedUser.email, user.email);
    assertEquals(expectedUser.florains, user.florains);
    assertEquals(expectedUser.sleepMode, user.sleepMode);
    assertEquals(expectedUser.conversations, user.conversations);
    assertEquals(expectedUser.transactionsExt, user.transactionsExt);
    assertEquals(expectedUser.transactionsIn, user.transactionsIn);
    assertEquals(expectedUser.pendingNotifications, user.pendingNotifications);
  }
}
