package QBank.LoginSignup.Repository;

import QBank.LoginSignup.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Branch findByBranchName(String branch);

    //int findBankIdByBranchName(String branchName);

    @Query("select branchName from Branch")
    List<String> getAllBranchName();
}