package eu.telecomnancy.labfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("JavaFx Demo");

        Button button = new Button("Click Me!");

        ImageView imageView = new ImageView(getClass().getResource("/eu/telecomnancy/labfx/images/kawai.png").toExternalForm());
        button.setGraphic(imageView);

        button.setOnAction(e -> {
            System.out.println("Bye!");
            Platform.exit();
        });

        Scene scene = new Scene(button, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
