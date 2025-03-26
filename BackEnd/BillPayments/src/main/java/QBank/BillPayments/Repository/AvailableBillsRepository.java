package QBank.BillPayments.Repository;

import QBank.BillPayments.Entity.AvailableBills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableBillsRepository extends JpaRepository<AvailableBills, Integer> {
}
