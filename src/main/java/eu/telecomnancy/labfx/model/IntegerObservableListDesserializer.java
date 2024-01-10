package eu.telecomnancy.labfx.model;

import java.lang.reflect.Type;
import jakarta.json.JsonNumber;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IntegerObservableListDesserializer implements JsonbDeserializer<ObservableList<Integer>> {

  @Override
  public ObservableList<Integer> deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    ObservableList<Integer> list = FXCollections.observableArrayList(
        parser.getArray().getValuesAs(JsonNumber.class).stream().map(v -> v.intValue()).toList());
    return list;
  }

}
