package QBank.CreditCard.Repository;

import QBank.CreditCard.Entity.AvailableCards;
import org.hibernate.id.IncrementGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableCardsRepository extends JpaRepository<AvailableCards, Integer> {

    @Query("select a from AvailableCards a")
    List<AvailableCards> getAll();
}
