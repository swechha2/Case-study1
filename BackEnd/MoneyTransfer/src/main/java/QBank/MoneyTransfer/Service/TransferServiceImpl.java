package QBank.MoneyTransfer.Service;

import QBank.MoneyTransfer.Communication.DFeignRest;
import QBank.MoneyTransfer.Communication.DFeignTransaction;
import QBank.MoneyTransfer.DTO.NomineeRequest;
import QBank.MoneyTransfer.DTO.NomineeTransferRequest;
import QBank.MoneyTransfer.DTO.TransferRequest;
import QBank.MoneyTransfer.Entity.Nominee;
import QBank.MoneyTransfer.Entity.Transfer;
import QBank.MoneyTransfer.Exceptions.NomineeDetailsNotFoundException;
import QBank.MoneyTransfer.Exceptions.NotEnoughBalanceException;
import QBank.MoneyTransfer.Repository.NomineeRepository;
import QBank.MoneyTransfer.Repository.TransferRepository;
import QBank.Transactions.Dto.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TransferServiceImpl implements TransferService{

    GenerateDetails gd = new GenerateDetails();
    double availableBalance;



    @Autowired
    TransferRepository transferRepository;

    @Autowired
    NomineeRepository nomineeRepository;

    @Autowired
    DFeignRest dFeignRest;

    @Autowired
    DFeignTransaction dFeignTransaction;



    @Override
    public void addNominee(NomineeRequest request) {
        GenerateDetails gd = new GenerateDetails();
        Nominee nominee = new Nominee();
        nominee.setNomineeId(gd.generateNomineeId());
        nominee.setName(request.getName());
        nominee.setAccountNumber(request.getAccountNumber());
        nominee.setBankId(request.getBankId());
        nominee.setBankName(request.getBankName());
        nominee.setNickName(request.getNickName());
        nominee.setRelRemarks(request.getRelRemarks());

        nomineeRepository.save(nominee);
    }

    @Override
    public Transfer moneyTransferWithoutNominee(TransferRequest request) throws NotEnoughBalanceException {
        availableBalance = dFeignRest.getDetails().getBalance();
        Transfer transfer = new Transfer();
        TransactionRequest Trequest = new TransactionRequest();
        // Go to payments gateway and after getting conformation the details are saved weather successful or not
        transfer.setTransferId(gd.generateTransferId(dFeignRest.getDetails().getBankId()));
        transfer.setToAccount(request.getToAccount());
        transfer.setFromAccount(dFeignRest.getDetails().getAccountNumber());
        transfer.setAmount(request.getAmount());
        transfer.setRemarks(request.getRemarks());
        transfer.setBankId(dFeignRest.getDetails().getBankId());
        transfer.setTransferDate(LocalDate.now());
        transfer.setTransferTime(LocalTime.now());
        Trequest.setTransactionId(transfer.getTransferId());
        Trequest.setAccountNumber(transfer.getToAccount());
        Trequest.setCustomerId(dFeignRest.getDetails().getCustomerId());
        Trequest.setAmount(request.getAmount());
        Trequest.setType("MoneyTransfer");
        Trequest.setDescription(request.getName()+" Money Transfer ");
        Trequest.setDate(LocalDate.now());
        Trequest.setTime(LocalTime.now());
        //balance = aviableBalance - request.getAmount();
        if(availableBalance <request.getAmount()){
            transfer.setStatus("Failed");
            Trequest.setStatus("Failed");
            transferRepository.save(transfer);
            dFeignTransaction.addTransaction(Trequest);
            throw new NotEnoughBalanceException("Insufficient Balance. Add funds and try again");
        }
        transfer.setStatus("Success");
        Trequest.setStatus("Success");
        availableBalance -= request.getAmount();
        //customerRepository.updateBalance(dFeignRest.getDetails().getCustomerId(), balance);
        dFeignRest.getDetails().setBalance(availableBalance);
        System.out.println(dFeignRest.getDetails().getBalance());
        transferRepository.save(transfer);
        dFeignTransaction.addTransaction(Trequest);
        return transfer;
    }

    @Override
    public Transfer moneyTransferWithNominee(NomineeTransferRequest request) throws NotEnoughBalanceException, NomineeDetailsNotFoundException {
        availableBalance = dFeignRest.getDetails().getBalance();
        Nominee nominee = nomineeRepository.findByName(request.getNomineeName());
        if(nominee ==null){
            throw new NomineeDetailsNotFoundException("Nominee not found/nAdd a new nominee");
        }
        Transfer transfer = new Transfer();
        TransactionRequest Trequest = new TransactionRequest();
        transfer.setTransferId(gd.generateTransferId(nominee.getBankId()));
        transfer.setFromAccount(request.getAccountNumber());
        transfer.setToAccount(nominee.getAccountNumber());
        transfer.setBankId(nominee.getBankId());
        transfer.setRemarks(request.getRemark());
        transfer.setAmount(request.getAmount());
        transfer.setTransferDate(LocalDate.now());
        transfer.setTransferTime(LocalTime.now());
        Trequest.setTransactionId(transfer.getTransferId());
        Trequest.setAccountNumber(transfer.getToAccount());
        Trequest.setCustomerId(dFeignRest.getDetails().getCustomerId());
        Trequest.setAmount(request.getAmount());
        Trequest.setType("MoneyTransfer");
        Trequest.setDescription(request.getNomineeName()+" Money Transfer ");
        Trequest.setDate(LocalDate.now());
        Trequest.setTime(LocalTime.now());

        if(availableBalance <request.getAmount()){
            Trequest.setStatus("Failed");
            transfer.setStatus("Failed");
            transferRepository.save(transfer);
            dFeignTransaction.addTransaction(Trequest);
            throw new NotEnoughBalanceException("Insufficient Balance. Add funds and try again");
        }
        transfer.setStatus("Success");
        Trequest.setStatus("Success");
        availableBalance -= request.getAmount();
        //customerRepository.updateBalance(dFeignRest.getDetails().getCustomerId(), balance);
        dFeignRest.getDetails().setBalance(availableBalance);
        System.out.println(dFeignRest.getDetails().getBalance());
        transferRepository.save(transfer);
        dFeignTransaction.addTransaction(Trequest);
        return transfer;
    }

    @Override
    public void saveToTransfer() {

    }

    @Override
    public double updateBalance() {
        return availableBalance;
    }

    @Override
    public Object test() {
        dFeignRest.getDetails().setBalance(1001.0011);
        return dFeignRest.getDetails().getBalance();
    }

    @Override
    public Map<String, Object> saveTransfer() {
      Map<String, Object> response = new HashMap<>();
      response.put("feild 1", dFeignRest.getDetails().getCustomerId());
        response.put("feild 2", dFeignRest.getDetails().getName());
        response.put("feild 3", dFeignRest.getDetails().getAccountNumber());
        response.put("feild 4", dFeignRest.getDetails().getBalance());
        return response;
    }
}



