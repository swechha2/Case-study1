package QBank.MoneyTransfer.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TransferRequest {

    private String name;
    private long fromAccount;
    private long toAccount;
    private double amount;
    private String remarks;
}
