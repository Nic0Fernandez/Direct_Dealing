package eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.*;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import javafx.fxml.FXMLLoader;

public class Main extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("DirectDealing");

        logingCreateScreen();
    }


    public void logingCreateScreen() throws IOException {
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/create_compte.fxml"));

        fxmloader.setControllerFactory((ic) -> new CreateAccount(this));

        Scene scene = new Scene(fxmloader.load(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mainScreen(User user) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/MainScreen.fxml"));
        Parent root = loader.load();

        MainScreenController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);

        Scene scene = new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void vueCreationOffre(User user) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/labfx/VueCreationOffre.fxml"));
        Parent root = loader.load();

        VueCreationOffreController controller = loader.getController();
        controller.setUser(user);
        controller.setMain(this);
        controller.initializeItems();

        Scene scene = new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
