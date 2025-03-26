package QBank.CreditCard.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AvailableCards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Type;

    private String payProvider;

    private double maxLimit;

}
