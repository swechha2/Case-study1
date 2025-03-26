package QBank.LoginSignup.DTO;

import lombok.Data;

@Data
public class SignupResponse {

    private long customerId;
    private long accountNumber;
    private double balance;
    private String name;
    private String email;
    private int bankId;
    private int score;
}
