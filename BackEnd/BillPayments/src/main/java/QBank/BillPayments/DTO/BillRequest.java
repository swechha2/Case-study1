package QBank.BillPayments.DTO;

import lombok.Data;

@Data
public class BillRequest {
    private String billType;
    private String billName;
    private int consumerId;
    private double amount;
}
