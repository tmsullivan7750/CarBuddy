package thomas.sullivan.carbuddy;

public class Vehicle {

    private String vin;
    private String year;
    private String make;
    private String model;
    private String engine;
    private String tanksize;
    private String citymileage;
    private String highwaymileage;
    private String userID;

    public Vehicle() {
        this.vin = "";
        this.year = "";
        this.make = "";
        this.model = "";
        this.engine = "";
        this.tanksize = "";
        this.citymileage = "";
        this.highwaymileage = "";
        this.userID = "";
    }

    public Vehicle(String aVin, String aYear, String aMake, String aModel, String aEngine, String aTankSize, String aCity, String aHighway, String aID){
        this.vin = aVin;
        this.year = aYear;
        this.make = aMake;
        this.model = aModel;
        this.engine = aEngine;
        this.tanksize = aTankSize;
        this.citymileage = aCity;
        this.highwaymileage = aHighway;
        this.userID = aID;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTankSize() {
        return tanksize;
    }

    public void setTankSize(String tankSize) {
        this.tanksize = tankSize;
    }

    public String getCityMileage() {
        return citymileage;
    }

    public void setCityMileage(String cityMileage) {
        this.citymileage = cityMileage;
    }

    public String getHighwayMileage() {
        return highwaymileage;
    }

    public void setHighwayMileage(String highwayMileage) {
        this.highwaymileage = highwayMileage;
    }

    public String getUserID(){
        return this.userID;
    }

    public void setUserID(String a) {
        this.userID = a;
    }


}
