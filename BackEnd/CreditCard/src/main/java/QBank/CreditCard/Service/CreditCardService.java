package QBank.CreditCard.Service;

import QBank.CreditCard.Dto.ApplyRequest;
import QBank.CreditCard.Entity.CreditCard;
import QBank.CreditCard.Exception.CardNotFoundException;
import QBank.CreditCard.Exception.InsufficientFundsException;
import QBank.Transactions.Dto.TransactionRequest;

import java.util.List;

public interface CreditCardService {


    void applyCC(ApplyRequest request);

    List<CreditCard> getAllActiveCC();

    List<CreditCard> getRejectCC();

    double getCurrentDue() ;

    TransactionRequest payCCBill(long id) throws CardNotFoundException, InsufficientFundsException;

    void cancelCC(long id) throws CardNotFoundException;
}
