package beer.happy_hour.drinking.model;

/**
 * Created by brcon on 13/03/2017.
 */

//public class DeliveryPlace implements Parcelable {
public class DeliveryPlace {

    private static DeliveryPlace instance;

    //adress
    private String adress;
    private String thoroughfare;  //rua
    private String subThoroughfare;  //numero aproximado
    private String subLocality;  //bairro
    private String locality;  //cidade
    private String cityState;
    private String adminArea; //state
    private String countryName;
    private String countryCode;
    private String zipCode;
    //place name
    private String featureName;  //nome do local, ou o numero aproximado (caso não tenha nome)
    //location adress
    private double latitude;
    private double longitude;
    private String complement;

    private Boolean obtainedInMap = false;

    private String DELIVERYPLACE_THOROUGHFATE_PREFIX = "Rua: ";
    private String DELIVERYPLACE_COMPLEMENT_PREFIX = "\nComplemento: ";
    private String DELIVERYPLACE_SUBLOCALITY_PREFIX = "\nBairro: ";
    private String DELIVERYPLACE_CITYSTATE_PREFIX = "\nCidade: ";

    //new data

//    private String countryName;

    private DeliveryPlace() {

    }

    public static DeliveryPlace getInstance() {
        if (instance == null) {
            synchronized (DeliveryPlace.class) {
                if (instance == null) {
                    instance = new DeliveryPlace();
                }
            }
        }

        return instance;
    }

    public boolean isValidPlace(){
        if( adress == null || adress.length() == 0 )
            return false;
        if(cityState == null || cityState.length() == 0)
            return false;
        if(countryName == null || countryName.length() == 0)
            return false;
        if(complement == null || complement.length() == 0)
            return false;

        return true;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getThoroughfare() {
        return thoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.thoroughfare = thoroughfare;
    }

    public String getSubThoroughfare() {
        return subThoroughfare;
    }

    public void setSubThoroughfare(String subThoroughfare) {
        this.subThoroughfare = subThoroughfare;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        if (featureName != null)
            this.featureName = featureName;
    }

    public Boolean wasObteinedInMap() {
        return obtainedInMap;
    }

    public void setObtainedInMap(Boolean obtainedInMap) {
        this.obtainedInMap = obtainedInMap;
    }

    @Override
    public String toString() {
        return "Adress: " + adress +
                "\nThoroughfare: " + thoroughfare +
                "\nSub Thoroughfare: " + subThoroughfare +
                "\nSub Locality: " + subLocality +
                "\nLocality: " + locality +
                "\nCity/State: " + cityState +
                "\nAdmin Area: " + adminArea +
                "\nCountry Name: " + countryName +
                "\nCountry Code: " + countryCode +
                "\nPostal CODE: " + zipCode +
                "\n" + "Feature Name: " + featureName +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude +
                "\ncomplement: " + complement;

    }

    public String printBrief() {
        return DELIVERYPLACE_THOROUGHFATE_PREFIX + thoroughfare +
                DELIVERYPLACE_COMPLEMENT_PREFIX + complement +
                DELIVERYPLACE_SUBLOCALITY_PREFIX + subLocality +
                DELIVERYPLACE_CITYSTATE_PREFIX + cityState;
    }

    public String printOrderDetails() {
        return DELIVERYPLACE_THOROUGHFATE_PREFIX + thoroughfare +
                DELIVERYPLACE_SUBLOCALITY_PREFIX + subLocality +
                DELIVERYPLACE_CITYSTATE_PREFIX + cityState;
    }
}
