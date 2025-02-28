public class RiderInfo {
    private String riderId;
    private String riderFullname;
    private String riderContactNo;
    private String zip;
    private String city;
    private String street;
    private String plateNumber;
    private String vehicle;
    private int rating;
    private String shipOnTime;

    public RiderInfo(String riderId, String riderFullname, String riderContactNo, String zip, String city, String street, String plateNumber, String vehicle, int rating, String shipOnTime) {
        this.riderId = riderId;
        this.riderFullname = riderFullname;
        this.riderContactNo = riderContactNo;
        this.zip = zip;
        this.city = city;
        this.street = street;
        this.plateNumber = plateNumber;
        this.vehicle = vehicle;
        this.rating = rating;
        this.shipOnTime = shipOnTime;
    }

    // Getters and setters for each field
    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public String getRiderFullname() { return riderFullname; }
    public void setRiderFullname(String riderFullname) { this.riderFullname = riderFullname; }

    public String getRiderContactNo() { return riderContactNo; }
    public void setRiderContactNo(String riderContactNo) { this.riderContactNo = riderContactNo; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getShipOnTime() { return shipOnTime; }
    public void setShipOnTime(String shipOnTime) { this.shipOnTime = shipOnTime; }
}