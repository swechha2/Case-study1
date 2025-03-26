package QBank.LoginSignup.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String branchName;

    private int branchId;

    private int bankId;
}
