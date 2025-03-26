package QBank.LoginSignup.DTO;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private int age;
    private String sex;
    private String address;
    private long phone;
    private String branchName;
    private Double Deposit;
    private String email;
    private String password;


}
