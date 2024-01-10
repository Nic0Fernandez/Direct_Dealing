package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.AdType;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainScreenController {

    @FXML
    private VBox adContainer;

    private Main main;
    private User user;

    public void setMain(Main main) {
        this.main = main;
        updateAds();
    }

    public void setUser(User user) {
        this.user = user;
        updateAds();
    }

    @FXML
    private void initialize() {
        
        updateAds();
    }

    @FXML
    private void ajouterOffre() throws IOException {
        main.viewCreateOffer(user);
    }

    @FXML
    private void displayProfil() {
    }

    @FXML
    private void displayMessagerie() throws IOException {
        main.inboxScreen(user);
    }

    
    public void updateAds() {
        List<Ad> allAds = new ArrayList<>(JSONDatabase.getInstance().getAdsAsList()); // Add stored ads
        allAds.addAll(createExampleAds()); // Add example ads
        displayAds(allAds);
    }

    private List<Ad> createExampleAds() {
        List<Ad> exampleAds = new ArrayList<>();

        Ad ad1 = new Ad();
        ad1.name = "Example Ad 1";
        ad1.type = AdType.GOOD;
        ad1.cost = 50;
        ad1.maxDistance = 10;
        ad1.start = new Date();
        ad1.end = new Date();
        ad1.disponibilities = "Example disponibilities";
        exampleAds.add(ad1);

        Ad ad2 = new Ad();
        ad2.name = "Example Ad 2";
        ad2.type = AdType.SERVICE;
        ad2.cost = 100;
        ad2.maxDistance = 20;
        ad2.start = new Date();
        ad2.end = new Date();
        ad2.disponibilities = "Example disponibilities";
        exampleAds.add(ad2);

        
        JSONDatabase.getInstance().getAdsAsList().addAll(exampleAds);

        return exampleAds;
    }

    private void displayAds(List<Ad> ads) {
        adContainer.getChildren().clear(); 
        for (Ad ad : ads) {
            HBox adBox = createAdBox(ad);
            adBox.setOnMouseClicked(event -> {
                try {
                    adDetail(ad);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
            adContainer.getChildren().add(adBox);   
        }
    }
    
    private HBox createAdBox(Ad ad) {
        HBox adBox = new HBox();
        Label nameLabel = new Label(ad.name);
        adBox.getChildren().add(nameLabel);
        return adBox;
    }

    public void adDetail(Ad offre) throws IOException{
        main.viewOffer(user, offre);
    }
}
