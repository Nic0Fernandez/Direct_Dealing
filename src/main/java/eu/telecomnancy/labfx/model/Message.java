package eu.telecomnancy.labfx.model;

public class Message {
  public final int id;
  public final String text;

  public Message(int id, String text) {
    this.id = id;
    this.text = text;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Message))
      return false;
    Message otherMessage = (Message) other;
    return this.id == otherMessage.id && this.text.equals(otherMessage.text);
  }

  @Override
  public String toString() {
    return String.valueOf(id) + ":\"" + text + "\"";
  }

}
