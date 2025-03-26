package QBank.BillPayments.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Bills {

    @Id
    private int billingId;

    private long customerId;

    private int consumerId;

    private String billType;

    private String billName;


}
