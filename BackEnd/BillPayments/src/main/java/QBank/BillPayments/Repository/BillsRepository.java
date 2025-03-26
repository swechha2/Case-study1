package QBank.BillPayments.Repository;

import QBank.BillPayments.Entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillsRepository extends JpaRepository<Bills, Integer> {
}
