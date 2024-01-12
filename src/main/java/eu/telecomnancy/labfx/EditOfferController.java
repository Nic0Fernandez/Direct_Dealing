package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import javafx.stage.FileChooser;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class EditOfferController {
    
    @FXML private TextField nom;
    @FXML private ComboBox type;
    @FXML private ComboBox nature;
    @FXML private TextField description;
    @FXML private TextField cout;
    @FXML private TextField localisation;
    @FXML private TextField distance;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField duree;
    @FXML private TextField disponibilites;
    @FXML private Button photo;
    @FXML private Button valider;
    @FXML private Button retour;
    @FXML private Label imagePath;
    private Ad existingOffer;

    private Main main;
    private Ad offre = new Ad();
    private String pathImage = "";
    private boolean isOffer;
    private AdType adType;
    private User user;

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
        restrictIntegers(cout);
        restrictIntegers(distance);
        restrictIntegers(duree);
    }

    public void restrictIntegers(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML 
    private void validerPhoto(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            this.pathImage = JSONDatabase.getInstance().saveImage(file.getAbsolutePath());  
            int i = pathImage.lastIndexOf('\\');
            String imageName = "";
            if (i > 0) {
                imageName = pathImage.substring(i+1);
            }
            imagePath.setText(imageName);
        }
    }

    public Date localDateToDate(LocalDate localDate){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    public void setOfferData(Ad existingOffer) {
        this.existingOffer = existingOffer;
        
        nom.setText(existingOffer.name);
        type.setValue(existingOffer.isOffer ? "Offre" : "Demande");
        nature.setValue(existingOffer.type == AdType.SERVICE ? "Service" : "Bien");
        description.setText(existingOffer.description);
        cout.setText(String.valueOf(existingOffer.cost));
        localisation.setText(existingOffer.address);
        distance.setText(String.valueOf(existingOffer.maxDistance));
        dateDebut.setValue(existingOffer.start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dateFin.setValue(existingOffer.end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        duree.setText(String.valueOf(existingOffer.duration));
        disponibilites.setText(existingOffer.disponibilities);
        
       
    }

    @FXML
    private void validerCreation() throws IOException{
        try {
            offre.userID = user.UID;

            if(nom.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.name = nom.getText();
            }
            
            if(localisation.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.address = localisation.getText();
            }
            
            if(cout.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.cost = Integer.parseInt(cout.getText());
            }
            
            try {
                offre.start = localDateToDate(dateDebut.getValue());
                offre.end = localDateToDate(dateFin.getValue());
                offre.isOffer= Offer();
                offre.type = type();
            } catch (Exception e) {
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }  
            
            if(duree.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.duration = Integer.parseInt(duree.getText());
            }

            if(disponibilites.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.disponibilities = disponibilites.getText();
            }
            
            if(description.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.description= description.getText();
            }

            if(type.getValue().equals("Offre")){
                if(pathImage.equals("") && offre.type==AdType.GOOD){
                showErrorMessage("Vous devez proposer une image de votre offre");
                return;
                }
                else{
                    offre.imagePath = pathImage;
                }
            }
            if(type.getValue().equals("Demande")){
                if(pathImage.equals("")){
                    offre.imagePath= "";
                }
                else{
                    offre.imagePath = pathImage;
                }
            }
            
            if(distance.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            else{
                offre.maxDistance = Integer.parseInt(distance.getText());
            }
            
            JSONDatabase.getInstance().updateAd(existingOffer);

           
            showErrorMessage("Vous devez remplir tous les champs");
        
    
        } catch (NumberFormatException e) {
            showErrorMessage("Vous devez remplir tous les champs");
            return;
        } 
    }

    public void showErrorMessage(String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void retourMain()throws IOException{
        System.out.println(existingOffer.getName());
        main.viewOfferProfil(user, existingOffer);
    }

}