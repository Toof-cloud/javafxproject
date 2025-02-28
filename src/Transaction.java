import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String bookingId;
    private String customerId;
    private String riderId; // Add riderId field
    private LocalDateTime pickupTime;
    private LocalDateTime arrivalTime;
    private double amountPaid;
    private String paymentStatus;
    private LocalDateTime transactionDate;

    public Transaction(String transactionId, String bookingId, String customerId, String riderId, LocalDateTime pickupTime, LocalDateTime arrivalTime, double amountPaid, String paymentStatus, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.riderId = riderId; // Initialize riderId
        this.pickupTime = pickupTime;
        this.arrivalTime = arrivalTime;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.transactionDate = transactionDate;
    }

    // Getters and setters for each field
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRiderId() { return riderId; } // Add getter for riderId
    public void setRiderId(String riderId) { this.riderId = riderId; } // Add setter for riderId

    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}// working