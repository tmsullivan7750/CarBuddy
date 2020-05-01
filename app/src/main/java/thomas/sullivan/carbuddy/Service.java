package thomas.sullivan.carbuddy;

import java.util.Date;

public class Service {

    private String title;
    private String type;
    private Date date;
    private String price;
    private String shop;
    private String location;
    private String vin;
    //Store Receipt Variable

    public Service()
    {
        this.title = "";
        this.type = "";
        this.date = null;
        this.price = "";
        this.shop = "";
        this.location = "";
        this.location = "";
    }

    public Service(String aName, String aType, Date bDate, String cPrice, String dName, String eLocation)
    {
        this.title = aName;
        this.type = aType;
        this.date = bDate;
        this.price = cPrice;
        this.shop = dName;
        this.location = eLocation;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shopName) {
        this.shop = shopName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
