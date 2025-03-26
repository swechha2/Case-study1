package QBank.BillPayments.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AvailableBills {

    @Id
    private int billId;

    private String billName;

    private String type;

    //private boolean reminder;

}
