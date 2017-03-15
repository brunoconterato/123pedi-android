package beer.happy_hour.drinking.model;

/**
 * Created by brcon on 13/03/2017.
 */

//public class DeliveryPlace implements Parcelable {
public class DeliveryPlace {

    private static DeliveryPlace instance;

    //adress
    private String adress;
    private String cityState;
    private String country;
    private String zipCode;
    //place name
    private String knownName;
    //location adress
    private double latitude;
    private double longitude;

    private String Complement;

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

    //    public DeliveryPlace() {
//
//    }

//    public DeliveryPlace(Parcel in) {
//        id = in.readInt();
//        adress = in.readString();
//        cityState = in.readString();
//        country = in.readString();
//        zipCode = in.readString();
//        knownName = in.readString();
//        latitude = in.readDouble();
//        longitude = in.readDouble();
//    }

//    public DeliveryPlace(int id, String adress, String cityState, String country, String zipCode, String knownName, double latitude, double longitude) {
//        this.id = id;
//        this.adress = adress;
//        this.cityState = cityState;
//        this.country = country;
//        this.zipCode = zipCode;
//        this.knownName = knownName;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    public String getComplement() {
        return Complement;
    }

    public void setComplement(String complement) {
        Complement = complement;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        if (knownName != null)
            this.knownName = knownName;
    }

    @Override
    public String toString() {
        return "Adress: " + adress +
                "\nCity/State: " + cityState +
                "\nCountry: " + country +
                "\nPostal CODE: " + zipCode +
                "\n" + "Place Name: " + knownName +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude;

    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeInt(id);
//        out.writeString(adress);
//        out.writeString(cityState);
//        out.writeString(country);
//        out.writeString(zipCode);
//        out.writeString(knownName);
//        out.writeDouble(latitude);
//        out.writeDouble(longitude);
//    }
//
//    public static final Creator<DeliveryPlace> CREATOR = new Creator<DeliveryPlace>() {
//        @Override
//        public DeliveryPlace createFromParcel(Parcel in) {
//            return new DeliveryPlace(in);
//        }
//
//        @Override
//        public DeliveryPlace[] newArray(int size) {
//            return new DeliveryPlace[size];
//        }
//    };
}
