package QBank.Admin.Dto;


import lombok.Data;

@Data
public class CreditCardRequest {

    private String Type;
    private String payProvider;
    private double maxLimit;

}

