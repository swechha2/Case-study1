package QBank.MoneyTransfer.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Nominee {

    @Id
    private int nomineeId;

    private String name;

    private String relRemarks;

    private long accountNumber;

    private String bankName;

    private int bankId;

    private String nickName;

}
