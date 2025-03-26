package QBank.PaymentGateWay.Repository;

import QBank.PaymentGateWay.Entity.PaymentGateWay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<PaymentGateWay, Long> {


    Optional<PaymentGateWay> findByUsername(String username);
}
