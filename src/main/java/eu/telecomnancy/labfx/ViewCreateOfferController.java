package eu.telecomnancy.labfx;

import java.io.IOException;

import eu.telecomnancy.labfx.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.geometry.Side;

import java.io.File;
import javafx.stage.FileChooser;
import java.util.Date;
import java.util.List;

import javafx.util.Callback;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ViewCreateOfferController {
    
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
    @FXML private Label errorMessage;
    @FXML private ContextMenu suggestions;

    private Main main;
    private Ad offre = new Ad();;
    private String pathImage = "";
    private boolean isOffer;
    private AdType adType;
    private User user;

    Distance distanceVille = new Distance();
    List<String> nomsVilles = distanceVille.getNomsVilles();

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
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(
                                    dateDebut.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }   
                            long d = ChronoUnit.DAYS.between(
                                    dateDebut.getValue(), item
                            );
                            setTooltip(new Tooltip(
                                "La durée de votre " + type.getValue() + " est de "+ d + " jours")
                            );
                    }
                };
            }
        };
        dateFin.setDayCellFactory(dayCellFactory);

        localisation.textProperty().addListener((observable, oldValue, newValue) -> {
            suggestions.getItems().clear();

            if (!newValue.isEmpty()) {
                int count = 0;
                for (String nomVille : nomsVilles) {
                    if (nomVille.toLowerCase().startsWith(newValue.toLowerCase())) {
                        MenuItem item = new MenuItem(nomVille);
                        item.setOnAction(event -> localisation.setText(nomVille));
                        suggestions.getItems().add(item);
                        count++;
                        if (count >= 7) {  
                            break;
                        }
                    }
                }

                if (!suggestions.getItems().isEmpty()) {
                    suggestions.show(localisation, Side.BOTTOM, 0, 0);
                } else {
                    suggestions.hide();
                }
            } else {
                suggestions.hide();
            }
        });
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
            if(!nomsVilles.contains(localisation.getText())){
                showErrorMessage("Le nom de la ville est invalide");
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
                offre.setOffer(true); 
                offre.type = type();
            } catch (Exception e) {
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }  

            long d = ChronoUnit.DAYS.between(dateDebut.getValue(), dateFin.getValue());
            
            if(duree.getText().isBlank()){
                showErrorMessage("Vous devez remplir tous les champs");
                return;
            }
            if(Integer.parseInt(duree.getText())>d){
                showErrorMessage("La durée que vous avez indiquée est trop importante");
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
                if(pathImage.equals("")){
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
            
            JSONDatabase.getInstance().addAd(offre);
            main.mainScreen(user);
        } catch (NumberFormatException e) {
            showErrorMessage("Vous devez remplir tous les champs");
            return;
        } 
    }

    public void showErrorMessage(String message){
        errorMessage.setText(message);
    }

    @FXML
    public void retourMain()throws IOException{
        main.mainScreen(user);
    }

}
