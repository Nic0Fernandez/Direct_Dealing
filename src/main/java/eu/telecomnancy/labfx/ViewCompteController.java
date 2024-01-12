package eu.telecomnancy.labfx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.JSONDatabase;
import eu.telecomnancy.labfx.model.Transaction;
import eu.telecomnancy.labfx.model.User;

public class ViewCompteController {

    @FXML private Label userName;
    @FXML private ImageView userPhoto;
    @FXML private ListView<Ad> offersListView;
    @FXML private ListView<Ad> demandsListView;
    @FXML private Button addPhotoButton;
    @FXML private Button backToMainButton;
    @FXML private ListView<Integer> notificationsView;

    private Main main;
    private User user;

    public void setMain(Main main){
        this.main = main;
    }

    public void setUser(User user){
        this.user = user;
        
        userName.setText(user.username);

        
        loadUserPhoto();

        loadUserOffersAndDemands();
    }

    private void loadUserPhoto() {
        String photoPath = user.imgpath;
        if (photoPath != null && !photoPath.isEmpty()) {
            File photoFile = new File(photoPath);
            if (photoFile.exists()) {
                Image image = new Image(photoFile.toURI().toString());
                userPhoto.setImage(image);
            }
        }
    }

    private void loadUserOffersAndDemands() {
        List<Ad> allAds = JSONDatabase.getInstance().getAdsAsList();

        List<Ad> userAds = allAds.stream()
                .filter(ad -> ad.userID == user.UID)
                .collect(Collectors.toList());

        List<Ad> userOffers = userAds.stream()
                .filter(ad -> ad.isOffer)
                .collect(Collectors.toList());

        List<Ad> userDemands = userAds.stream()
                .filter(ad -> !ad.isOffer)
                .collect(Collectors.toList());

        offersListView.setItems(FXCollections.observableArrayList(userOffers));
        demandsListView.setItems(FXCollections.observableArrayList(userDemands));

        offersListView.setCellFactory(param -> new ListCell<Ad>() {
            @Override
            protected void updateItem(Ad ad, boolean empty) {
                super.updateItem(ad, empty);

                if (empty || ad == null || ad.getName() == null) {
                    setText(null);
                } else {
                    setText(ad.getName());
                }
            }
        });

        demandsListView.setCellFactory(param -> new ListCell<Ad>() {
            @Override
            protected void updateItem(Ad ad, boolean empty) {
                super.updateItem(ad, empty);

                if (empty || ad == null || ad.getName() == null) {
                    setText(null);
                } else {
                    setText(ad.getName());
                }
            }
        });

        offersListView.setOnMouseClicked(event -> {
            Ad selectedAd = offersListView.getSelectionModel().getSelectedItem();
            if (selectedAd != null) {
                try {
                    main.viewOfferProfil(user, selectedAd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        demandsListView.setOnMouseClicked(event -> {
            Ad selectedAd = demandsListView.getSelectionModel().getSelectedItem();
            if (selectedAd != null) {
                try {
                    main.viewOfferProfil(user, selectedAd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void addPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            user.imgpath = file.getAbsolutePath();
            
            loadUserPhoto();
        }
    }
    @FXML
    private void editPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a new photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
    
        if (file != null) {
            user.imgpath = file.getAbsolutePath();
    
            loadUserPhoto();
        }
    }
    
    @FXML
    private void backToMain() throws IOException {
        main.mainScreen(user);
    }
}
