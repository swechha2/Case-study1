package QBank.Admin.Dto;

import lombok.Data;

@Data
public class LoanRequest {

    private double interestRate;
    private double maxLoanAmount;
    private double penalty;
    private String loanType;

}
