package QBank.Transactions.Service;

import QBank.Transactions.Dto.TransactionRequest;
import QBank.Transactions.Entity.Transactions;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    void addTransaction(TransactionRequest request);

    List<Transactions> getAll();

    List<Transactions> getBetweenDates(LocalDate from, LocalDate to);

    List<Transactions> getLastHundred();

    List<Transactions> getByType(String type);
}
