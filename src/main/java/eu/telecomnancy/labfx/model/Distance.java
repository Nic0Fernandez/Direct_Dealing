package eu.telecomnancy.labfx.model;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

import java.util.HashMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Distance {

    private Map<String, Ville> villes = new HashMap<>();

    public Distance(){
        loadCities();
    }

    private void loadCities(){
        try (InputStream is = getClass().getResourceAsStream("/eu/telecomnancy/labfx/data/cities.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                Jsonb jsonb = JsonbBuilder.create();
                DatabaseVilles dbVilles = jsonb.fromJson(reader, DatabaseVilles.class);
    
                for (Ville ville : dbVilles.getCities()) {
                    String nomVille = ville.getLabel();
                    String nomVilleMaj = nomVille.substring(0, 1).toUpperCase() + nomVille.substring(1).toLowerCase();
                    villes.put(nomVilleMaj, ville);
                } 
                System.out.println("Aperçu des villes chargées :");
                int count = 0;
                for (Map.Entry<String, Ville> entry : villes.entrySet()) {
                    System.out.println("Clé (Nom de la ville) : " + entry.getKey() + ", Valeur (Objet Ville) : " + entry.getValue());
                    count++;
                    if (count >= 10) { 
                        break;
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getNomsVilles() {
        return new ArrayList<>(villes.keySet());
    }

    public double calculerDistance(String nomVille1, String nomVille2){
        String nomVille1Maj = nomVille1.substring(0, 1).toUpperCase() + nomVille1.substring(1).toLowerCase();
        String nomVille2Maj = nomVille2.substring(0, 1).toUpperCase() + nomVille2.substring(1).toLowerCase();

        Ville ville1 = villes.get(nomVille1Maj);
        Ville ville2 = villes.get(nomVille2Maj);

    if (ville1 == null || ville2 == null) {
            throw new IllegalArgumentException("Une des villes n'est pas trouvée dans la base de données.");
        }

    return haversine(ville1.getLatitude(), ville1.getLongitude(), ville2.getLatitude(), ville2.getLongitude());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        Double latDistance = lat2Rad-lat1Rad;
        Double lonDistance = lon2Rad-lon1Rad;

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}

