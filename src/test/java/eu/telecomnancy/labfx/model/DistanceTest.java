package eu.telecomnancy.labfx.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DistanceTest {
    
    private Distance distance;

    @BeforeEach
    public void setUp() {
        distance = new Distance();
    }

    @Test
    public void testDistanceEntreDeuxVilles() {

        String ville1 = "paris";
        String ville2 = "lyon";

        double distanceCalcul = distance.calculerDistance(ville1, ville2);

       
        double distanceAttendue = 392.0; 
        double tolerance = 10.0; 

        assertEquals(distanceAttendue, distanceCalcul, tolerance);
    }
}
