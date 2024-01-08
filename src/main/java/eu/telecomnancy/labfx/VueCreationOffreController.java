package main.java.eu.telecomnancy.labfx;

import eu.telecomnancy.labfx.model.Ad;
import eu.telecomnancy.labfx.model.AdType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private Ad offre;
    private String pathImage;
    private boolean isOffer;
    private AdType adType;

    private boolean Offer(){
        if(type.getValue().equals("Offre")){
            return 1;
        }
        else{
            return 0;
        }
    }

    private AdType type(){
        if(nature.getValue().equals("Service")){
            return AdType.SERVICE;
        }
        else{
            return AdType.GOOD;
        }
    }

    @FXML
    private void validerCreation(){
        offre = new Ad();
        offre.setName(nom.getText());
        offre.setAddress(localisation.getText());
        offre.setCost(cout.getText());
        offre.setStart(dateDebut.getValue());
        offre.setEnd(dateFin.getValue());
        offre.setDisponibilities(disponibilites.getText());
        offre.setDescription(description.getText());
        offre.setImagePath(pathImage);
        offre.isOffer(Offer());
        offre.setType(type());
        offre.setMaxDistance(distance.getText());

        

    }



}
