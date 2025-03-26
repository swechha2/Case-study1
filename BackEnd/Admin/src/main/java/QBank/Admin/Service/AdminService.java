package QBank.Admin.Service;

import QBank.Admin.Dto.BranchRequest;
import QBank.Admin.Dto.CreditCardRequest;
import QBank.Admin.Dto.LoanRequest;

public interface AdminService {

    SignupResponse addAdmin(AdminRequest adminRequest);

    LoginResponse login(LoginRequest loginRequest);

    BranchRequest addBranch(BranchRequest branchRequest);

    CreditCardRequest addCards(CreditCardRequest creditCardRequest);

    LoanRequest addLoan (LoanRequest loanRequest);



}
