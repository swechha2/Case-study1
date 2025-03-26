package QBank.CreditCard.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int creditCardId;

    private long customerId;

    private String cardType;

    private String payProvider;

    private double creditLimit;

    private long creditCardNumber;

    private int cvv;

    private double currentDue;

    private int pin;

    private String expiry;

    private LocalDate issueDate;

    private String holderName;

    private String isActive;

    private String status;
    
}
