package QBank.AccountUpdate.DTO;


import lombok.Data;

@Data
public class UpdateRequest {
    private String name;
    private String address;
    private int age;
    private String sex;
    private String email;
    private long phone;
}
