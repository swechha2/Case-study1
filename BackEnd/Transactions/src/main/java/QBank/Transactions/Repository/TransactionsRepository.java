package QBank.Transactions.Repository;

import QBank.Transactions.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByDateBetween(LocalDate from, LocalDate to);

    @Query("select t from Transactions t order by t.date limit 100")
    List<Transactions> getLastHundred();

    List<Transactions> findByType(String type);
}
