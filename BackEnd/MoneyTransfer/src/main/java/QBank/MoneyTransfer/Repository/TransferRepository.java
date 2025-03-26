package QBank.MoneyTransfer.Repository;

import QBank.MoneyTransfer.Entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

}
