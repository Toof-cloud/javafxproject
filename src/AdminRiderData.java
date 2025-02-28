public class AdminRiderData {
    private String riderId;
    private String riderName;
    private String plateNo;
    private String vehicle;
    private String city;
    private String street;
    private String zip;
    private String contactNo;
    private double rating;
    private String shipOnTime;

    public AdminRiderData(String riderId, String riderName, String plateNo, String vehicle, String city, String street, String zip, String contactNo, double rating, String shipOnTime) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.plateNo = plateNo;
        this.vehicle = vehicle;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.contactNo = contactNo;
        this.rating = rating;
        this.shipOnTime = shipOnTime;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getContactNo() {
        return contactNo;
    }

    public double getRating() {
        return rating;
    }

    public String getShipOnTime() {
        return shipOnTime;
    }
}