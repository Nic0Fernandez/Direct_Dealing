package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.*;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputFilter.Status;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
    @FXML private Label reservationLabel;
    @FXML private Button messageButton;
    @FXML private Button reserveButton;
    @FXML private ImageView image;

    private Main main;
    private Ad offer;
    private User user;
    private String imagePath;

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

    public Image loadImage(){
        String imagePath = offer.imagePath;
        if(imagePath!=null && !imagePath.isEmpty()){
            System.out.println("Loading image");
            File file = new File(imagePath);
            String imageFileUrl = file.toURI().toString();
            Image imageLoaded = new Image(imageFileUrl);
            return imageLoaded;
        }
        else{
            System.out.println("Failed to load image");
            return null;
        }
    }

    public void initializeItems(){
        nom.setText(offer.name);
        if(offer.isOffer()){
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
        if(dateFin != null){
            dateFin.setText(dateToString(offer.end));
        }
        else{
            dateFin.setText("");
        }
        if(duree!=null){
            duree.setText(Integer.toString(offer.duration));
        }
        else{
            duree.setText("");
        }
        disponibilites.setText(offer.disponibilities);
        image.setImage(loadImage());
        checkStatus();
    }

    

    public void checkStatus(){
        if(user.transactionsExt.containsKey(offer.ID)){
            int transactionID = user.transactionsExt.get(offer.ID);
            Transaction transaction = JSONDatabase.getInstance().getTransaction(transactionID);
            if(transaction.statusType == StatusType.RESERVED){
                reserveButton.setText("Waiting for answer...");
                reserveButton.setDisable(true);
            }
            if(transaction.statusType == StatusType.REFUSED){
                reserveButton.setText("Refusée");
                reserveButton.setDisable(true);
            }
            if(transaction.statusType == StatusType.ACCEPTED){
                reserveButton.setText("Complete");
                reserveButton.setDisable(false);
                reserveButton.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent event){
                        transaction.statusType = StatusType.COMPLETED;
                        updateDatabase(transaction,StatusType.COMPLETED);
                        reservationLabel.setText("Complétée ! Les florains ont été transférés");
                    }
                });
            }
        } else if (user.florains < 0 && offer.isOffer()) {
            reserveButton.setDisable(true);
            reservationLabel.setText("pas permi de reserver une offre avec solde negatif!");
        }
    }

    public void updateDatabase(Transaction transaction, StatusType statusType){
        JSONDatabase.getInstance().saveStatus(transaction, statusType);
    }
    
    @FXML
    public void retourMainScreen() throws IOException{
        System.out.println(user.transactionsExt);
        System.out.println(user.transactionsIn);
        main.mainScreen(user);
    }

    @FXML 
    public void sendMessage() throws IOException{
        main.inboxScreen(user, offer, null);
    }

    @FXML 
    public void reserve(){
        int transactionID = user.createTransaction(offer);
        reservationLabel.setText("Offre réservée");
        reserveButton.setText("Waiting for answer");
        reserveButton.setDisable(true);
    }
}
