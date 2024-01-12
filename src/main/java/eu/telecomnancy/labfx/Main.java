package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.*;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private User currentUser;

    private final int width = 800;
    private final int height = 600;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("DirectDealing");

        if (currentUser == null) {
            loginScreen(); // Show login screen if no user is logged in
        } else {
            mainScreen(currentUser); // Show the main screen if a user is already logged
        }

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Exit...");
            javafx.application.Platform.exit();
        });
    }

    private void show(Parent p) {
        Scene scene = new Scene(p, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loginScreen() throws IOException {
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Login.fxml"));

        fxmloader.setControllerFactory(ic -> new Login(this));
        show(fxmloader.load());
    }

    public void createAccountScreen() throws IOException {
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/create_compte.fxml"));
        fxmloader.setControllerFactory(ic -> new CreateAccount(this));

        show(fxmloader.load());
    }

    public void mainScreen(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/MainScreen.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(MainScreenController.class))
                return new MainScreenController();
            else if (ic.equals(DirectDealingMenuController.class))
                return new DirectDealingMenuController(this, user, DirectDealingMenuController.Screen.MAIN);
            return null;
        });

        Parent root = loader.load();

        MainScreenController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);

        show(root);
    }

    public void viewCreateOffer(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ViewCreateOffer.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(ViewCreateOfferController.class))
                return new ViewCreateOfferController();
            else if (ic.equals(DirectDealingMenuController.class))
                return new DirectDealingMenuController(this, user, DirectDealingMenuController.Screen.CREATE);
            return null;
        });

        Parent root = loader.load();

        ViewCreateOfferController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);
        controller.initializeItems();
        
        show(root);
        
    }

    public void inboxScreen(User user, Ad offer, User receiver) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Inbox.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(InboxScreen.class))
                return new InboxScreen(this, user, offer, receiver);
            else if (ic.equals(ConversationView.class))
                return new ConversationView(this, user);
            else if (ic.equals(DirectDealingMenuController.class))
                return new DirectDealingMenuController(this, user, DirectDealingMenuController.Screen.INBOX);
            return null;
        });

        show(loader.load());
    }

    public void ViewCompteController(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ViewCompte.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(ViewCompteController.class))
                return new ViewCompteController();
            else if (ic.equals(DirectDealingMenuController.class))
                return new DirectDealingMenuController(this, user, DirectDealingMenuController.Screen.PROFILE);
            return null;
        });
        Parent root = loader.load();

        ViewCompteController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);

        show(root);
    }

    public void viewOffer(User user, Ad offer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ViewOffer.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(ViewOfferController.class))
                return new ViewOfferController();
            else if (ic.equals(DirectDealingMenuController.class))
                return new DirectDealingMenuController(this, user, DirectDealingMenuController.Screen.VIEW);
            return null;
        });
        Parent root = loader.load();

        ViewOfferController controller = loader.getController();
        controller.setUser(user);
        controller.setAd(offer);
        controller.setMain(this);
        controller.initializeItems();

        show(root);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
