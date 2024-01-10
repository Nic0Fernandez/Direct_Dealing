package eu.telecomnancy.labfx.model;

public class AdHistory {
    public int id;
    public Ad ad;
    public StatusType statusType;

    public AdHistory(Ad ad, StatusType statusType) {
        this.ad = ad;
        this.statusType = statusType;
    }

    
}
