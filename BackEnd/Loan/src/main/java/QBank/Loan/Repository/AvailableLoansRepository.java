package QBank.Loan.Repository;

import QBank.Loan.Entity.AvailableLoans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableLoansRepository extends JpaRepository<AvailableLoans, Integer> {

     AvailableLoans findByLoanType(String type);
}
