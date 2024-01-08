package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class UserTest {

  @Test
  void serializeTest() {
    User user = new User("1");
    user.username = "Lucas Skora";
    user.password = "password";
    user.address = "193 Av. Paul Muller, 54600 Villers-lès-Nancy";
    user.email = "e2455u@telecomnancy.net";
    user.florains = 10;
    
    Jsonb jsonb = JsonbBuilder.create();
    String jsonUser = jsonb.toJson(user);

    String expected = "{\"UID\":\"1\",\"address\":\"193 Av. Paul Muller, 54600 Villers-lès-Nancy\",\"email\":\"e2455u@telecomnancy.net\",\"florains\":10,\"password\":\"password\",\"sleepMode\":false,\"username\":\"Lucas Skora\"}";

    assertEquals(expected, jsonUser);
  }
}
