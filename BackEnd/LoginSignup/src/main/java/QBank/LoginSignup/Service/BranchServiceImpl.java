package QBank.LoginSignup.Service;

import QBank.LoginSignup.DTO.BranchRequest;
import QBank.LoginSignup.Entity.Branch;
import QBank.LoginSignup.Repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService{

    @Autowired
    BranchRepository branchRepository;

    @Override
    public Branch addBranch(BranchRequest request){
        Branch branch = new Branch();
        branch.setBranchName(request.getBranchName());
        branch.setBranchId(request.getBranchId());
        branch.setBankId(request.getBankId());
        branchRepository.save(branch);
        return branch;
    }


    @Override
    public List<Branch> getAllBranchs(){
        return branchRepository.findAll();
    }

    @Override
    public Branch findByBranchName(String name) {
        return branchRepository.findByBranchName(name);
    }
}
