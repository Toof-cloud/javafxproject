public class SessionManager {
    private static String customerId;

    public static void setCustomerId(String id) {
        customerId = id;
    }

    public static String getCustomerId() {
        return customerId;
    }

    public static void clearSession() {
        customerId = null;
    }
}
