package QBank.LoginSignup.DTO;

import lombok.Data;

@Data
public class UpdateDetails {
    private String name;
    private String email;
    private String sex;
    private String address;
    private int age;
    private long phone;
}
