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
                .filter(ad -> ad.offer)
                .collect(Collectors.toList());

        List<Ad> userDemands = userAds.stream()
                .filter(ad -> !ad.offer)
                .collect(Collectors.toList());

        offersListView.setItems(FXCollections.observableArrayList(userOffers));
        demandsListView.setItems(FXCollections.observableArrayList(userDemands));
        notificationsView.setItems(user.pendingNotifications);

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
        notificationsView.setCellFactory(param -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer transactionID, boolean empty) {
                super.updateItem(transactionID, empty);

                if (empty || transactionID == null) {
                    setGraphic(null);
                    return;
                } else {
                    Transaction t = JSONDatabase.getInstance().getTransaction(transactionID);
                    if (t == null) return;
                    Node n = null;
                    try {
                        n = createNotification(t);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (n == null) {
                        setVisible(false);
                        setManaged(false);
                    } else {
                        setGraphic(n);
                    } 

                }
            }

            private Node createNotification(Transaction t) throws IOException {
                FXMLLoader loader;
                switch (t.statusType) {
                case RESERVED:
                    loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/reservationNotification.fxml"));
                    loader.setControllerFactory((ic) -> new ReservationNotification(main, t));
                    return loader.load();
                case ACCEPTED:
                case COMPLETED:
                case REFUSED:
                    loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/DismissableNotification.fxml"));
                    loader.setControllerFactory((ic) -> new DismissableNotification(main, t, user));
                    return loader.load();
                case NEUTRAL:
                default:
                    return null;
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
