package eu.telecomnancy.labfx.model;

import java.lang.reflect.Type;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MessageListDesserializer implements JsonbDeserializer<ObservableList<Message>> {

  @Override
  public ObservableList<Message> deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    ObservableList<Message> list = FXCollections.observableArrayList();
    JsonArray jsonArray = parser.getArray();
    for (JsonValue value : jsonArray) {
      JsonObject object = value.asJsonObject();
      list.add(new Message(object.getInt("id"), object.getString("text")));
    }
    return list;
  }

}
