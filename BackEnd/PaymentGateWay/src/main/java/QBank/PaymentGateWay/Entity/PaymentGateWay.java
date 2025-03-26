package QBank.PaymentGateWay.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PaymentGateWay {

    @Id
    private long customerId;

    private String username;

    private String password;
}
