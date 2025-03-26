package QBank.Loan.Service;

import QBank.Loan.DTO.ApplyLoanRequest;
import QBank.Loan.Entity.AvailableLoans;
import QBank.Loan.Entity.Loan;
import QBank.Loan.Exception.InsufficientBalanceException;
import QBank.Loan.Exception.NoLoanFound;

import java.util.List;
import java.util.Map;

public interface LoanService {

    void applyForLoan(ApplyLoanRequest request);

    List<Loan> getAllApprovedLoans();

    List<Loan> getAllRejectedLoans();

    List<AvailableLoans> getAllLoanService();

    Map<String, Object> payLoan(String id) throws NoLoanFound, InsufficientBalanceException;

}
