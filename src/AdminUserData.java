import java.time.LocalDateTime;

public class AdminUserData {
    private String userId;
    private String username;
    private String password;
    private LocalDateTime accountCreated;

    public AdminUserData(String userId, String username, String password, LocalDateTime accountCreated) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.accountCreated = accountCreated;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getAccountCreated() {
        return accountCreated;
    }
}