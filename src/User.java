import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleStringProperty userid;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty accountcreated;

    public User(String uid, String uname, String pword, String acreated) {
        this.userid = new SimpleStringProperty(uid);
        this.username = new SimpleStringProperty(uname);
        this.password = new SimpleStringProperty(pword);
        this.accountcreated = new SimpleStringProperty(acreated);
    }

    // Getter methods
    public String getUserid() {
        return userid.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getAccountcreated() {
        return accountcreated.get();
    }

    // Setter methods
    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Property getters (for JavaFX binding)
    public SimpleStringProperty useridProperty() {
        return userid;
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public SimpleStringProperty accountcreatedProperty() {
        return accountcreated;
    }
}