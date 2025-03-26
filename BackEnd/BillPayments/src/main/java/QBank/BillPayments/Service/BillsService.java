package QBank.BillPayments.Service;

import QBank.BillPayments.DTO.BillRequest;
import QBank.BillPayments.Entity.Bills;
import QBank.BillPayments.Exception.BillNotFoundException;
import QBank.CreditCard.Exception.InsufficientFundsException;
import QBank.Transactions.Dto.TransactionRequest;

import java.util.List;

public interface BillsService {

    void addBill(BillRequest request);

    void deleteBill(int billId) throws BillNotFoundException;

    List<Bills> getAllBills();

    TransactionRequest payBill(int billId) throws BillNotFoundException, InsufficientFundsException;
}
