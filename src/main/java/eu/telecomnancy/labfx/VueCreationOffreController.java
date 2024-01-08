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

public class VueCreationOffreController {
    
    @FXML private TextField nom;
    @FXML private ComboBox type;
    @FXML private ComboBox nature;
    @FXML private TextArea description;
    @FXML private TextField cout;
    @FXML private TextField localisation;
    @FXML private TextField distance;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField duree;
    @FXML private TextField disponibilites;
    @FXML private Button photo;
    @FXML private Button valider;

    private Main main;
    private Ad offre;
    private String pathImage;
    private boolean isOffer;
    private AdType adType;
    private User user;
    private int adID = 0;

    public void setMain(Main main){
        this.main=main;
    }

    public void setUser(User user){
        this.user=user;
    }

    public boolean Offer(){
        if(type.getValue().equals("Offre")){
            return true;
        }
        else{
            return false;
        }
    }

    public AdType type(){
        if(nature.getValue().equals("Service")){
            return AdType.SERVICE;
        }
        else{
            return AdType.GOOD;
        }
    }

    public void initializeItems() {
        type.getItems().removeAll(type.getItems());
        type.getItems().addAll("Offre", "Demande");
        nature.getItems().removeAll(nature.getItems());
        nature.getItems().addAll("Service", "Bien");
    }

    @FXML 
    private void validerPhoto(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            this.pathImage=file.getAbsolutePath();

            try{
                BufferedImage image = ImageIO.read(new File(pathImage));
                this.pathImage="src/main/resources/eu/telecomnancy/labfx/images/" + new File(pathImage).getName();
                String extension = "";
                int i = pathImage.lastIndexOf(".");
                if (i>0){
                    extension = pathImage.substring(i+1);
                }
                try{
                    ImageIO.write((RenderedImage)image,extension,new File(pathImage));
                } catch(Exception e) {
                    System.out.println("Cannot write image");
                }    
            } catch(Exception e){
                System.out.println("Cannot read image");
            }  
        }
    }

    public Date localDateToDate(LocalDate localDate){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    @FXML
    private void validerCreation() throws IOException{
        offre = new Ad();
        offre.userID = user.UID;
        offre.name = nom.getText();
        offre.address = localisation.getText();
        offre.cost = Integer.parseInt(cout.getText());
        offre.start = localDateToDate(dateDebut.getValue());
        offre.end = localDateToDate(dateFin.getValue());
        offre.disponibilities = disponibilites.getText();
        offre.description = description.getText();
        offre.imagePath = pathImage;
        offre.isOffer= Offer();
        offre.type = type();
        offre.maxDistance = Integer.parseInt(distance.getText());
        offre.adID = adID;
        adID++;
        
        main.mainScreen(user);
    }



}
