package QBank.MoneyTransfer.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Transfer {

    @Id
    private long transferId;

    private long fromAccount;

    private long toAccount;

    private double amount;

    private int bankId;

    private String remarks;

    private String status;

    private LocalDate transferDate;

    private LocalTime transferTime;


}
