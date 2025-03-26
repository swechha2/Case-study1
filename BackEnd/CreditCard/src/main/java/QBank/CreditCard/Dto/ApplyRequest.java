package QBank.CreditCard.Dto;

import lombok.Data;

@Data
public class ApplyRequest {
    //private long customerId;
    private String type;
    private String payProvider;
    private String holderName;
    private double maxLimit;
}
