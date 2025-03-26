package QBank.Loan.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AvailableLoans {

    @Id
    private int loanId;

    private double interestRate;

    private double maxLoanAmount;

    private double penalty;

    private String loanType;
}
