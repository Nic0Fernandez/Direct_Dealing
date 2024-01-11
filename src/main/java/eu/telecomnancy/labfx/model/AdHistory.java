package eu.telecomnancy.labfx.model;

public class AdHistory {
    public int ID;
    public int UID;
    public Ad ad;
    public StatusType statusType;

    public AdHistory(){}

    public AdHistory(Ad ad, StatusType statusType) {
        this.ad = ad;
        this.statusType = statusType;
    }

    
}
