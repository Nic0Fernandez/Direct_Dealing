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
    }

    public void loginScreen() throws IOException {
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Login.fxml"));

        // Assuming you have a LoginController class for the login screen
        fxmloader.setControllerFactory(ic -> new Login(this));

        Scene scene = new Scene(fxmloader.load(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createAccountScreen() throws IOException {
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/create_compte.fxml"));

        // Assuming you have a CreateAccountController class for the account creation
        // screen
        fxmloader.setControllerFactory(ic -> new CreateAccount(this));

        Scene scene = new Scene(fxmloader.load(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mainScreen(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/MainScreen.fxml"));
        Parent root = loader.load();

        MainScreenController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void viewCreateOffer(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ViewCreateOffer.fxml"));
        Parent root = loader.load();

        ViewCreateOfferController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);
        controller.initializeItems();

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void inboxScreen(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/Inbox.fxml"));
        loader.setControllerFactory((ic) -> {
            if (ic.equals(InboxScreen.class))
                return new InboxScreen(this, user);
            else if (ic.equals(ConversationView.class))
                return new ConversationView(this, user);
            return null;
        });

        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void viewOffer(User user, Ad offer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/ViewOffer.fxml"));
        Parent root = loader.load();

        ViewOfferController controller = loader.getController();
        controller.setUser(user);
        controller.setAd(offer);
        controller.setMain(this);
        controller.initializeItems();

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
