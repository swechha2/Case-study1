package QBank.Transactions.Service;

import QBank.LoginSignup.DTO.SharedDetails;
import QBank.Transactions.Communication.FeignCustDetails;
import QBank.Transactions.Dto.TransactionRequest;
import QBank.Transactions.Entity.Transactions;
import QBank.Transactions.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    FeignCustDetails sharedDetails;

    @Override
    public void addTransaction(TransactionRequest request) {
        Transactions transactions = new Transactions();
        transactions.setTransactionId(request.getTransactionId());
        transactions.setAmount(request.getAmount());
        transactions.setType(request.getType());
        transactions.setDate(request.getDate());
        transactions.setTime(request.getTime());
        transactions.setAccountNumber(request.getAccountNumber());
        transactions.setCustomerId(request.getCustomerId());
        transactions.setDescription(request.getDescription());
        transactions.setStatus(request.getStatus());
       transactionsRepository.save(transactions);
    }

    @Override
    public List<Transactions> getAll() {
        return transactionsRepository.findAll();
    }

    @Override
    public List<Transactions> getBetweenDates(LocalDate from, LocalDate to) {
        return transactionsRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Transactions> getLastHundred() {
        return transactionsRepository.getLastHundred();
    }

    @Override
    public List<Transactions> getByType(String type) {
        return transactionsRepository.findByType(type);
    }
}


