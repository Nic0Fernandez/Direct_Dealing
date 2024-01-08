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
    ad.duration = Duration.ofSeconds(1);
    ad.disponibilities = "never";
    Jsonb jsonb = JsonbBuilder.create();
    String jsonAd = jsonb.toJson(ad);

    String expected = "{\"ID\":1,\"address\":\"0, street of the fools\",\"cost\":100,\"description\":\"baba\",\"disponibilities\":\"never\",\"duration\":\"PT1S\",\"end\":\"2001-05-11T00:00:00Z[UTC]\",\"imagePath\":\"/bi/bi.jpg\",\"isOffer\":true,\"maxDistance\":100.0,\"name\":\"ad name\",\"offer\":true,\"start\":\"2000-11-27T00:00:00Z[UTC]\",\"type\":\"GOOD\"}";

    assertEquals(expected, jsonAd);
  }
}
