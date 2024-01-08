package eu.telecomnancy.labfx.model;

import java.time.Duration;
import java.util.Date;

public class Ad {
  public int ID;
  public String name;
  public boolean isOffer;
  public AdType type;
  public String description;
  public String imagePath;
  public int cost;
  public String address;
  public double maxDistance;
  public Date start;
  public Date end;
  public Duration duration;
  public String disponibilities;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public boolean isOffer() {
    return isOffer;
  }
  public void setOffer(boolean isOffer) {
    this.isOffer = isOffer;
  }
  public AdType getType() {
    return type;
  }
  public void setType(AdType type) {
    this.type = type;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getImagePath() {
    return imagePath;
  }
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }
  public int getCost() {
    return cost;
  }
  public void setCost(int cost) {
    this.cost = cost;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public double getMaxDistance() {
    return maxDistance;
  }
  public void setMaxDistance(double maxDistance) {
    this.maxDistance = maxDistance;
  }
  public Date getStart() {
    return start;
  }
  public void setStart(Date start) {
    this.start = start;
  }
  public Date getEnd() {
    return end;
  }
  public void setEnd(Date end) {
    this.end = end;
  }
  public Duration getDuration() {
    return duration;
  }
  public void setDuration(Duration duration) {
    this.duration = duration;
  }
  public String getDisponibilities() {
    return disponibilities;
  }
  public void setDisponibilities(String disponibilities) {
    this.disponibilities = disponibilities;
  }
}
