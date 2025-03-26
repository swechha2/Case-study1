package QBank.LoginSignup.DTO;

import lombok.Data;

@Data
public class SharedDetails {
    private long accountNumber;
    private long customerId;
    private String name;
    private int bankId;
    private double balance;
    private int score;

}
