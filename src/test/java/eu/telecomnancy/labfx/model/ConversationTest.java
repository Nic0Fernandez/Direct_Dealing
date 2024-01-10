package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class ConversationTest {
  @Test
  void testAddMessage() {
    Conversation conv = new Conversation(1, 2);
    conv.addMessage(1, "hello");
    assertEquals(1, conv.getMessages().size());
  }

  @Test
  void testAddMessageWrongUser() {
    Conversation conv = new Conversation(1, 2);
    conv.addMessage(3, "hello");
    assertEquals(0, conv.getMessages().size());
  }

  @Test
  void serializeTest() {
    Conversation conv = new Conversation(0, 1);
    conv.id = 10;
    conv.addMessage(0, "a");
    conv.addMessage(1, "b");
    conv.addMessage(1, "c");
    Jsonb jsonb = JsonbBuilder.create();
    String json = jsonb.toJson(conv);

    String expected = "{\"id\":10,\"messages\":[{\"id\":0,\"text\":\"a\"},{\"id\":1,\"text\":\"b\"},{\"id\":1,\"text\":\"c\"}],\"userIds\":[0,1]}";

    assertEquals(expected, json);
  }

  @Test
  void deserializeTest() {
    Jsonb jsonb = JsonbBuilder.create();
    String jsonString = "{\"id\":15,\"messages\":[{\"id\":2,\"text\":\"a\"},{\"id\":1,\"text\":\"b\"},{\"id\":1,\"text\":\"c\"}],\"userIds\":[1,2]}";

    Conversation expected = new Conversation(1, 2);
    expected.id = 15;
    expected.addMessage(2, "a");
    expected.addMessage(1, "b");
    expected.addMessage(1, "c");

    Conversation convo = jsonb.fromJson(jsonString, Conversation.class);

    assertEquals(expected.id, convo.id);
    assertEquals(expected.getMessages(), convo.getMessages());
    assertEquals(expected.getUserIds(), convo.getUserIds());
  }

}
