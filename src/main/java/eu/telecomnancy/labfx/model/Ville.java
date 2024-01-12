package eu.telecomnancy.labfx.model;

public class Ville {
    private String insee_code;
    private String city_code;
    private String zip_code;
    private String label;
    private String latitude;
    private String longitude;
    private String department_name;
    private String departement_number;
    private String region_name;
    private String region_geojson_name;
    
    public String getInsee_code() {
        return insee_code;
    }
    public void setInsee_code(String insee_code) {
        this.insee_code = insee_code;
    }
    public String getCity_code() {
        return city_code;
    }
    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }
    public String getZip_code() {
        return zip_code;
    }
    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public double getLatitude() {
        try {
            return Double.parseDouble(latitude);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        try {
            return Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getDepartment_name() {
        return department_name;
    }
    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
    public String getDepartement_number() {
        return departement_number;
    }
    public void setDepartement_number(String departement_number) {
        this.departement_number = departement_number;
    }
    public String getRegion_name() {
        return region_name;
    }
    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
    public String getRegion_geojson_name() {
        return region_geojson_name;
    }
    public void setRegion_geojson_name(String region_geojson_name) {
        this.region_geojson_name = region_geojson_name;
    }



}
