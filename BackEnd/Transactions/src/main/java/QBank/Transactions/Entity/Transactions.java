package QBank.Transactions.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long customerId;

    private long transactionId;

    private long accountNumber;

    private String type;

    private LocalDate date;

    private LocalTime time;

    private double amount;

    private String status;

    private String description;
}
