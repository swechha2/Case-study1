package QBank.LoginSignup.Service;

import QBank.LoginSignup.DTO.BranchRequest;
import QBank.LoginSignup.Entity.Branch;

import java.util.List;

public interface BranchService {

    Branch addBranch(BranchRequest request);

    List<Branch> getAllBranchs();

    Branch findByBranchName(String name);
}
