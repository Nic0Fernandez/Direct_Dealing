package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.AdType;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainScreenController {

    @FXML
    private VBox adContainer;
    @FXML private ComboBox typeFiltre;
    @FXML private ComboBox evaluationFiltre;

    @FXML
    private ComboBox<AdType> typeFiltre;

    @FXML
    private TextField nomFiltre;

    @FXML
    private TextField coutFiltre;

    @FXML
    private TextField distanceFiltre;

    @FXML
    private DatePicker dateDebutFiltre;

    @FXML
    private DatePicker dateFinFiltre;

    @FXML
    private TextField evaluationFiltre;

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
        typeFiltre.setItems(FXCollections.observableArrayList(AdType.values()));
        nomFiltre.textProperty().addListener((observable, oldValue, newValue) -> updateAds());
        typeFiltre.valueProperty().addListener((observable, oldValue, newValue) -> updateAds());
        coutFiltre.textProperty().addListener((observable, oldValue, newValue) -> updateAds());
        distanceFiltre.textProperty().addListener((observable, oldValue, newValue) -> updateAds());
        dateDebutFiltre.valueProperty().addListener((observable, oldValue, newValue) -> updateAds());
        dateFinFiltre.valueProperty().addListener((observable, oldValue, newValue) -> updateAds());
        updateAds();
    }

    @FXML
    private void ajouterOffre() throws IOException {
        main.viewCreateOffer(user);
    }

    @FXML
    private void displayCompte(ActionEvent event) throws IOException {
        main.ViewCompteController(user);
    }

    @FXML
    private void displayMessagerie() throws IOException {
        main.inboxScreen(user, null);
    }

    
    public void updateAds() {
        List<Ad> allAds = new ArrayList<>(JSONDatabase.getInstance().getAdsAsList()); // Add stored ads
        //allAds.addAll(createExampleAds());

        
        List<Ad> filteredAds = applyFilters(allAds);
        //allAds.addAll(createExampleAds()); // Add example ads
        displayAds(filteredAds);
    }

    private List<Ad> applyFilters(List<Ad> ads) {
        String nomFilter = nomFiltre.getText().trim().toLowerCase();
        ads = ads.stream()
                 .filter(ad -> ad.name.toLowerCase().contains(nomFilter))
                 .collect(Collectors.toList());
    
        // Filter by type
        AdType selectedType = typeFiltre.getValue();
        if (selectedType != null) {
            ads = ads.stream().filter(ad -> ad.type == selectedType).collect(Collectors.toList());
        }
 
        if (!coutFiltre.getText().isEmpty()) {
            String cout = coutFiltre.getText();
            if (cout != null) {
                Integer countInteger = Integer.parseInt(cout);
                ads = ads.stream().filter(ad -> ad.cost <= countInteger).collect(Collectors.toList());
            }
        }

        if (!distanceFiltre.getText().isEmpty()) {
            String dist = distanceFiltre.getText();
            if (dist != null) {
                Double distDouble = Double.parseDouble(dist);
                ads = ads.stream().filter(ad -> ad.maxDistance <= distDouble).collect(Collectors.toList());
            }
        }

        LocalDate debutFilter = dateDebutFiltre.getValue();
        LocalDate finFilter = dateFinFiltre.getValue();
        if (debutFilter != null && finFilter != null) {
            ads = ads.stream()
                    .filter(ad -> {
                        LocalDate adStartDate = ad.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate adEndDate = ad.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return (adStartDate.isAfter(debutFilter) ||adStartDate.isEqual(debutFilter)) && (adEndDate.isBefore(finFilter) || adEndDate.isEqual(finFilter));
                    })
                    .collect(Collectors.toList());
        }
        return ads;
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
        HBox adBox = new HBox(10);
        Label nameLabel = new Label(ad.name);
        Label coutLabel = new Label(String.valueOf(ad.cost));
        Label distLabel = new Label(String.valueOf(ad.maxDistance));
        Label typeLabel = new Label(String.valueOf(ad.type));
        Label dateDebutLabel = new Label(String.valueOf(ad.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        Label dateFinLabel = new Label(String.valueOf(ad.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        adBox.getChildren().add(nameLabel);
        adBox.getChildren().add(coutLabel);
        adBox.getChildren().add(distLabel);
        adBox.getChildren().add(typeLabel);
        adBox.getChildren().add(dateDebutLabel);
        adBox.getChildren().add(dateFinLabel);

        return adBox;
    }

    
    public void adDetail(Ad offre) throws IOException{
        main.viewOffer(user, offre);
    }
}
