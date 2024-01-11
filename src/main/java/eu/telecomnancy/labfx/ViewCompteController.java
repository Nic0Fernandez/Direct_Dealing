package eu.telecomnancy.labfx;

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
    @FXML private ListView<String> offersListView;
    @FXML private ListView<String> demandsListView;
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

        
        List<String> userOffers = userAds.stream()
                .filter(ad -> ad.isOffer)
                .map(Ad::getName)
                .collect(Collectors.toList());

        List<String> userDemands = userAds.stream()
                .filter(ad -> !ad.isOffer)
                .map(Ad::getName)
                .collect(Collectors.toList());

        offersListView.getItems().addAll(userOffers);
        demandsListView.getItems().addAll(userDemands);
        notificationsView.setItems(user.pendingNotifications);

        notificationsView.setCellFactory(param -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer transactionID, boolean empty) {
                super.updateItem(transactionID, empty);

                if (empty || transactionID == null) {
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
             switch (t.statusType) {
                case RESERVED:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Inbox.fxml"));
                    loader.setControllerFactory((ic) -> new ReservationNotification(main, t));
                    return loader.load();
                case ACCEPTED:
                case COMPLETED:
                case NEUTRAL:
                case REFUSED:
                default:
                    return null;
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
    private void backToMain() throws IOException {
        
        main.mainScreen(user);
    }
}
