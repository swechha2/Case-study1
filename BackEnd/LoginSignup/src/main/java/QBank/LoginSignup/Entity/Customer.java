package QBank.LoginSignup.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="customer_info")
public class Customer {

    @Id
    private long customerId;

    private long accountNumber;

    private int bankId;

    @Column(nullable = false)
    private String branchName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String sex;
    @Column(nullable = false)
    private long phone;

    private double balance;

    @Column(unique = true, nullable = false)
    private String email;

    private String role;

    @Column(nullable = false)
    private String password;

    private int score;

}
