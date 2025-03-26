package QBank.Transactions.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TransactionRequest {
    private long customerId;
    private long transactionId;
    private long accountNumber;
    private double amount;
    private String type;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private String description;
}
