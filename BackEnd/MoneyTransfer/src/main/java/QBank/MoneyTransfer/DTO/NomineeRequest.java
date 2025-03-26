package QBank.MoneyTransfer.DTO;

import lombok.Data;

@Data
public class NomineeRequest {

    private String name;
    private String relRemarks;
    private long accountNumber;
    private String bankName;
    private int bankId;
    private String nickName;
}
