package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.AdType;
import eu.telecomnancy.labfx.model.Distance;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

import java.io.IOException;
import javafx.scene.image.ImageView;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainScreenController {

    @FXML
    private VBox adContainer;

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

    @FXML private TextField villeFiltre;

    @FXML private ContextMenu suggestions;
    @FXML
    private Label notificationsLabel;

    private Main main;
    private User user;

    Distance distanceVille = new Distance();
    List<String> nomsVilles = distanceVille.getNomsVilles();

    public void setMain(Main main) {
        this.main = main;
        updateAds();
    }

    public void setUser(User user) {
        this.user = user;
        updateAds();
        int notifications = user.pendingNotifications.size();
        String notificationsText = "";
        if (notifications == 1) {
            notificationsText = "1 nouvelle notification";
        }
        else if (notifications > 1) {
            notificationsText = String.valueOf(notifications) + " nouvelles notifications!";
        }
        notificationsLabel.setText(notificationsText);
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
        villeFiltre.textProperty().addListener((observable, oldValue, newValue) -> updateAds());
        typeFiltre.getSelectionModel().select(2);
        updateAds();

        villeFiltre.textProperty().addListener((observable, oldValue, newValue) -> {
            suggestions.getItems().clear();

            if (!newValue.isEmpty()) {
                int count = 0;
                for (String nomVille : nomsVilles) {
                    if (nomVille.toLowerCase().startsWith(newValue.toLowerCase())) {
                        MenuItem item = new MenuItem(nomVille);
                        item.setOnAction(event -> villeFiltre.setText(nomVille));
                        suggestions.getItems().add(item);
                        count++;
                        if (count >= 7) {  
                            break;
                        }
                    }
                }

                if (!suggestions.getItems().isEmpty()) {
                    suggestions.show(villeFiltre, Side.BOTTOM, 0, 0);
                } else {
                    suggestions.hide();
                }
            } else {
                suggestions.hide();
            }
        });
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
        main.inboxScreen(user, null, null);
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
    
        AdType selectedType = typeFiltre.getValue();
        if (selectedType != null && selectedType != AdType.ALL) {
            ads = ads.stream()
                .filter(ad -> ad.type == selectedType)
                .collect(Collectors.toList());
        }

        if (!villeFiltre.getText().isBlank() && (distanceFiltre.getText() == null || distanceFiltre.getText().trim().isEmpty())) {
            String villeFilter = villeFiltre.getText().trim();
            ads = ads.stream()
                    .filter(ad -> ad.address.contains(villeFilter))
                    .collect(Collectors.toList());
        }
 
        if (!coutFiltre.getText().isEmpty()) {
            String cout = coutFiltre.getText();
            if (cout != null) {
                Integer countInteger = Integer.parseInt(cout);
                ads = ads.stream().filter(ad -> ad.cost <= countInteger).collect(Collectors.toList());
            }
        }

        if (!distanceFiltre.getText().isBlank() && !villeFiltre.getText().isBlank()) {
            String villeDuFiltre = villeFiltre.getText();
            String dist = distanceFiltre.getText();
            if(dist != null){
                Double distDouble = Double.parseDouble(dist);
                ads = ads.stream()
                    .filter(ad -> distanceVille.calculerDistance(villeDuFiltre, ad.address) <= distDouble)
                    .collect(Collectors.toList());
            }
            if(dist == null){
                ads = ads.stream()
                    .filter(ad -> ad.address.contains(villeDuFiltre))
                    .collect(Collectors.toList());
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

    public Image loadImage(Ad ad){
        String imagePath = ad.imagePath;
        if(imagePath!=null && !imagePath.isEmpty()){
            File file = new File(imagePath);
            String imageFileUrl = file.toURI().toString();
            Image imageLoaded = new Image(imageFileUrl);
            return imageLoaded;
        }
        else{
            return null;
        }
    }

    private HBox createAdBox(Ad ad) {
        HBox adBox = new HBox(0);
        adBox.setPrefWidth(400);
        ImageView image = new ImageView();
        image.setImage(loadImage(ad));
        image.setFitHeight(100);
        image.setFitWidth(100);
        Label nameLabel = new Label(ad.name);
        nameLabel.setMinWidth(100);
        Label coutLabel = new Label(String.valueOf(ad.cost) + " florains");
        coutLabel.setMinWidth(100);
        Label dateFinLabel = new Label(String.valueOf(ad.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        dateFinLabel.setMinWidth(100);
        adBox.getChildren().add(nameLabel);
        adBox.getChildren().add(coutLabel);
        adBox.getChildren().add(dateFinLabel);
        adBox.getChildren().add(image);

        return adBox;
    }

    
    public void adDetail(Ad offre) throws IOException{ 
        main.viewOffer(user, offre);
    }
}
