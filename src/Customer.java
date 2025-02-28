public class Customer {
    private String customerId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String street;
    private String city;
    private String zipCode;
    private String password;

    public Customer(String customerId, String fullName, String email, String contactNumber, String street, String city, String zipCode, String password) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.password = password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPassword() {
        return password;
    }

    // Constructor
    public Customer(String fullName, String email, String contactNumber, String street, String city, String zipCode, String password) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.password = password;
    }
}