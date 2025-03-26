package QBank.LoginSignup.DTO;

import lombok.Data;

@Data
public class BranchRequest {
    private String branchName;
    private int branchId;
    private int bankId;
}
