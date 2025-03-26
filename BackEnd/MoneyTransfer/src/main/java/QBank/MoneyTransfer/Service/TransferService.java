package QBank.MoneyTransfer.Service;

import QBank.MoneyTransfer.DTO.NomineeRequest;
import QBank.MoneyTransfer.DTO.NomineeTransferRequest;
import QBank.MoneyTransfer.DTO.TransferRequest;
import QBank.MoneyTransfer.Entity.Transfer;
import QBank.MoneyTransfer.Exceptions.NomineeDetailsNotFoundException;
import QBank.MoneyTransfer.Exceptions.NotEnoughBalanceException;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface TransferService {

    void addNominee(NomineeRequest request);

    Transfer moneyTransferWithoutNominee(TransferRequest request) throws NotEnoughBalanceException;

   Transfer moneyTransferWithNominee(NomineeTransferRequest request) throws NotEnoughBalanceException, NomineeDetailsNotFoundException;


    void saveToTransfer();

    double updateBalance();

    Object test();

    Map<String, Object> saveTransfer();
    //other methods to be added later
}
