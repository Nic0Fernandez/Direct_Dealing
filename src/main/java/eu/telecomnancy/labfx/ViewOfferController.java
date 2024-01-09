package eu.telecomnancy.labfx;

import javafx.fxml.FXML;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.User;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class ViewOfferController {
    @FXML private Label nom;
    @FXML private Label type;
    @FXML private Label description;
    @FXML private Label cout;
    @FXML private Label localisation;
    @FXML private Label distanceMax;
    @FXML private Label dateDebut;
    @FXML private Label dateFin;
    @FXML private Label duree;
    @FXML private Label disponibilites;
    @FXML private Button messageButton;
    @FXML private Button ReserveButton;

    private Main main;
    private Ad offer;
    private User user;

    public void setMain(Main main){
        this.main=main;
    }

    public void setAd(Ad offer){
        this.offer=offer;
    }

    public void setUser(User user){
        this.user=user;
    }

    public String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);  
        return strDate; 
    }

    public void initializeItems(){
        nom.setText(offer.name);
        if(offer.isOffer){
            type.setText("Offre");
        }
        else{
            type.setText("Demande");
        }
        description.setText(offer.description);
        cout.setText(Integer.toString(offer.cost));
        localisation.setText(offer.address);
        distanceMax.setText(offer.maxDistance + "");
        dateDebut.setText(dateToString(offer.start));
        dateFin.setText(dateToString(offer.end));
        duree.setText(Integer.toString(offer.duration));
        disponibilites.setText(offer.disponibilities);

    }

    @FXML 
    public void sendMessage(){

    }

    @FXML 
    public void reserve(){

    }






}
