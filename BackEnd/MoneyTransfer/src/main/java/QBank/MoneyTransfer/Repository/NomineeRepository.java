package QBank.MoneyTransfer.Repository;

import QBank.MoneyTransfer.Entity.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Integer> {

    Nominee findByName(String name);
}
