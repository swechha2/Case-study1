package QBank.CreditCard.Repository;

import QBank.CreditCard.Entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    @Query("select c from CreditCard c where c.isActive='active'")
    List<CreditCard> getActive();

    @Query("select c from CreditCard c where c.status='denied'")
    List<CreditCard> getReject();
}
