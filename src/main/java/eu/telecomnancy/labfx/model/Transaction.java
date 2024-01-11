package eu.telecomnancy.labfx.model;

public class Transaction {
    public int ID; //transaction ID
    public int UID; //user ID
    public int adID;
    public StatusType statusType;

    public Transaction(){}

    public Transaction(Ad ad,int UID, StatusType statusType) {
        this.adID = ad.ID;
        this.UID = UID;
        this.statusType = statusType;
    }
}
