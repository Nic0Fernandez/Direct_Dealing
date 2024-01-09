package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

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

    Jsonb jsonb = JsonbBuilder.create();
    String jsonUser = jsonb.toJson(user);

    String expected = "{\"UID\":1,\"address\":\"193 Av. Paul Muller, 54600 Villers-lès-Nancy\",\"email\":\"e2455u@telecomnancy.net\",\"florains\":10,\"password\":\"password\",\"sleepMode\":false,\"username\":\"Lucas Skora\"}";

    assertEquals(expected, jsonUser);
  }

  @Test
  void deserializeTest() {
    Jsonb jsonb = JsonbBuilder.create();
    String jsonString = "{\"UID\":10,\"address\":\"c\",\"email\":\"d\",\"florains\":500,\"password\":\"b\",\"sleepMode\":false,\"username\":\"a\"}";
    
    User expectedUser = new User();
    expectedUser.UID = 10;
    expectedUser.username = "a";
    expectedUser.password = "b";
    expectedUser.address = "c";
    expectedUser.email = "d";
    expectedUser.florains = 500;
    expectedUser.sleepMode = false;
  
    User user = jsonb.fromJson(jsonString, User.class);
    
    assertEquals(expectedUser.UID, user.UID);
    assertEquals(expectedUser.username, user.username);
    assertEquals(expectedUser.password, user.password);
    assertEquals(expectedUser.address, user.address);
    assertEquals(expectedUser.email, user.email);
    assertEquals(expectedUser.florains, user.florains);
    assertEquals(expectedUser.sleepMode, user.sleepMode);
  }
}
