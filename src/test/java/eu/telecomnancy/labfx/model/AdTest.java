package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class AdTest {

  Date buildDate(int year, int month, int day) {
    return Date.from(LocalDate.of(year, month, day).atStartOfDay().toInstant(ZoneOffset.UTC));
  }

  @Test
  void serializeTest() {
    Ad ad = new Ad();
    ad.ID = 1;
    ad.userID = 10;
    ad.name = "ad name";
    ad.isOffer = true;
    ad.type = AdType.GOOD;
    ad.description = "baba";
    ad.imagePath = "/bi/bi.jpg";
    ad.cost = 100;
    ad.address = "0, street of the fools";
    ad.maxDistance = 100;
    ad.start = buildDate(2000, 11, 27);
    ad.end = buildDate(2001, 5, 11);
    ad.duration = 1;
    ad.disponibilities = "never";
    Jsonb jsonb = JsonbBuilder.create();
    String jsonAd = jsonb.toJson(ad);

    String expected = "{\"ID\":1,\"address\":\"0, street of the fools\",\"cost\":100,\"description\":\"baba\",\"disponibilities\":\"never\",\"duration\":1,\"end\":\"2001-05-11T00:00:00Z[UTC]\",\"imagePath\":\"/bi/bi.jpg\",\"isOffer\":true,\"maxDistance\":100.0,\"name\":\"ad name\",\"start\":\"2000-11-27T00:00:00Z[UTC]\",\"type\":\"GOOD\",\"userID\":10}";

    assertEquals(expected, jsonAd);
  }

  @Test
  void deserializeTest() {
    Jsonb jsonb = JsonbBuilder.create();
    String jsonString = "{\"ID\":5,\"address\":\"a\",\"cost\":50,\"description\":\"b\",\"disponibilities\":\"not\",\"duration\":5,\"end\":\"2001-06-11T00:00:00Z[UTC]\",\"imagePath\":\"d\",\"isOffer\":false,\"maxDistance\":100.0,\"name\":\"e\",\"start\":\"2000-11-28T00:00:00Z[UTC]\",\"type\":\"SERVICE\",\"userID\":30}";

    Ad expectedAd = new Ad();
    expectedAd.ID = 5;
    expectedAd.userID = 30;
    expectedAd.name = "e";
    expectedAd.isOffer = false;
    expectedAd.type = AdType.SERVICE;
    expectedAd.description = "b";
    expectedAd.imagePath = "d";
    expectedAd.cost = 50;
    expectedAd.address = "a";
    expectedAd.maxDistance = 100.0;
    expectedAd.start = buildDate(2000, 11, 28);
    expectedAd.end = buildDate(2001, 6, 11);
    expectedAd.duration = 5;
    expectedAd.disponibilities = "not";

    Ad ad = jsonb.fromJson(jsonString, Ad.class);

    assertEquals(expectedAd.ID, ad.ID);
    assertEquals(expectedAd.userID, ad.userID);
    assertEquals(expectedAd.name, ad.name);
    assertEquals(expectedAd.isOffer, ad.isOffer);
    assertEquals(expectedAd.type, ad.type);
    assertEquals(expectedAd.description, ad.description);
    assertEquals(expectedAd.imagePath, ad.imagePath);
    assertEquals(expectedAd.cost, ad.cost);
    assertEquals(expectedAd.address, ad.address);
    assertEquals(expectedAd.maxDistance, ad.maxDistance);
    assertEquals(expectedAd.start, ad.start);
    assertEquals(expectedAd.end, ad.end);
    assertEquals(expectedAd.duration, ad.duration);
    assertEquals(expectedAd.disponibilities, ad.disponibilities);
  }
}
