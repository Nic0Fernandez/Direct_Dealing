package eu.telecomnancy.labfx.model;

public class User {
  public int UID;
  public String username;
  public String password;
  public String address;
  public String email;
  public int florains;
  public boolean sleepMode;
  public String imgpath ;

  public User() {
    sleepMode = false;
  }

  public int getUID() {
    return UID;
}

public String getUserName() {
    return username;
}

public String getPhotoPath() {
    return imgpath;
}

public void setPhotoPath(String pathImage) {
  pathImage=this.imgpath ;
}
}


