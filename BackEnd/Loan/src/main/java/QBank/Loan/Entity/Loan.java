package QBank.Loan.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Loan {

    @Id
    private String custLoanId;

    private long accountNumber;

    private int loanId;

    private long customerId;

    private String loanType;

    private double loanAmount;

    private int tenure;

    private String frequency;

    private LocalDate date;

    private double nextPayable;

    private double totalPayable;

    private boolean ongoing;

    private LocalDate dueDate;

    private String approval;
}
