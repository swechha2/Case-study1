package QBank.Admin.Dto;

import lombok.Data;

@Data
public class BranchRequest {

    private String branchName;
    private int branchId;
    private int bankId;
}
