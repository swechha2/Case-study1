package QBank.Loan.Repository;

import QBank.Loan.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    @Query("select l from Loan l where l.approval='Approved'")
    List<Loan> getAllApprovedLoans();

    @Query("select l from Loan l where l.approval='Denied'")
    List<Loan> getAllRejectLoans();
}
