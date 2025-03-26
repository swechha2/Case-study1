package QBank.LoginSignup.Repository;

import QBank.LoginSignup.Entity.Customer;

import QBank.LoginSignup.Exception.UserNotFoundException;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    String getPasswordByEmail(String email);

    String getPasswordByCustomerId(long custId);

    Optional<Customer> findByCustomerId(long custId) ;

    Customer findByEmail(String email);
}
