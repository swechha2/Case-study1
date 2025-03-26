package QBank.LoginSignup.Repository;

import QBank.LoginSignup.Entity.Inlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InlogsRepository extends JpaRepository<Inlogs, Integer > {
}
