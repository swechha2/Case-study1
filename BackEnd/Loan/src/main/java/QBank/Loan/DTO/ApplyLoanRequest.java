package QBank.Loan.DTO;

import lombok.Data;

@Data
public class ApplyLoanRequest {
    private String loanType;
    private Double loanAmount;
    private int tenure;
    private String frequency;
}
