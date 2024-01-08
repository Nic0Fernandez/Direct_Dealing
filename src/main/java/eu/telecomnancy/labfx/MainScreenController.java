package eu.telecomnancy.labfx;


import java.io.IOException;
import eu.telecomnancy.labfx.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MainScreenController {
    
    @FXML private TextField nomFiltre;
    @FXML private ComboBox typeFiltre;
    @FXML private ComboBox evaluationFiltre;
    @FXML private TextField coutFiltre;
    @FXML private TextField distanceFiltre;
    @FXML private DatePicker dateDebutFiltre;
    @FXML private DatePicker dateFinFiltre;
    @FXML private Button boutonAjouter;
    @FXML private Button boutonProfil;
    @FXML private Button boutonMessagerie;

    private Main main;
    private User user;

    public void setMain(Main main){
        this.main=main;
    }

    public void setUser(User user){
        this.user=user;
    }

    @FXML
    private void ajouterOffre() throws IOException{
        main.vueCreationOffre(user);
    }

    @FXML
    private void displayProfil(){}

    @FXML 
    private void displayMessagerie(){}
}
