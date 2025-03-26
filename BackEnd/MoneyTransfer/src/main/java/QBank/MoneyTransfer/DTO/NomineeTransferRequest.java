package QBank.MoneyTransfer.DTO;

import lombok.Data;

@Data
public class NomineeTransferRequest {
    private String nomineeName;
    private double amount;
    private long accountNumber;
    private String remark;
}
