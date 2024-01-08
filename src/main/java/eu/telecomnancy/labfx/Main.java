package eu.telecomnancy.labfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
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

    public void mainScreen() {
        
    }
}
