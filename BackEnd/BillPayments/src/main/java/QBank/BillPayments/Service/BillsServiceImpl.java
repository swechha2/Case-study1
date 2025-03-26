package QBank.BillPayments.Service;

import QBank.BillPayments.Communication.DFeignRest;
import QBank.BillPayments.Communication.DFeignTransaction;
import QBank.BillPayments.DTO.BillRequest;
import QBank.BillPayments.Entity.Bills;
import QBank.BillPayments.Exception.BillNotFoundException;
import QBank.BillPayments.Repository.AvailableBillsRepository;
import QBank.BillPayments.Repository.BillsRepository;
import QBank.CreditCard.Communication.DFeignSharedDetails;
import QBank.CreditCard.Exception.InsufficientFundsException;
import QBank.LoginSignup.DTO.SharedDetails;
import QBank.Transactions.Dto.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BillsServiceImpl implements BillsService {

    GenerateDetails gd= new GenerateDetails();

    @Autowired
    AvailableBillsRepository availableBillsRepository;

    @Autowired
    BillsRepository billsRepository;

    @Autowired
    DFeignRest sharedDetails;

    @Autowired
    DFeignTransaction dFeignTransaction;


    @Override
    public void addBill(BillRequest request) {

        Bills bill = new Bills();
        bill.setCustomerId(sharedDetails.getDetails().getCustomerId());
        bill.setConsumerId(request.getConsumerId());
        bill.setBillName(request.getBillName());
        bill.setBillType(request.getBillType());
        bill.setBillingId(gd.generateBillerId());


        billsRepository.save(bill);
    }

    @Override
    public void deleteBill(int billId) throws BillNotFoundException {
        Optional<Bills> bill = billsRepository.findById(billId);
        if(bill.isEmpty())
            throw new BillNotFoundException("No such found try again with different id");
        billsRepository.delete(bill.get());
    }

    @Override
    public List<Bills> getAllBills() {
        return billsRepository.findAll();
    }

    @Override
    public TransactionRequest payBill(int billId) throws BillNotFoundException, InsufficientFundsException {
        Bills bill = billsRepository.findById(billId).orElseThrow(() -> new BillNotFoundException("No such bill found"));

        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(sharedDetails.getDetails().getCustomerId());
        request.setAccountNumber(bill.getConsumerId());
        request.setAmount(gd.generateBill());
        request.setType("Bill: "+bill.getBillType());
        request.setDate(LocalDate.now());
        request.setTime(LocalTime.now());
        request.setDescription(String.valueOf(bill.getBillingId()) + "Bill" + bill.getBillType());
        request.setTransactionId(gd.generateTransferId(sharedDetails.getDetails().getBankId()));
        if(sharedDetails.getDetails().getBalance()<request.getAmount()){
            request.setStatus("Failed");
            dFeignTransaction.addTransaction(request);
            throw new InsufficientFundsException("Current Balance not enough");
        }
        request.setStatus("Success");
        dFeignTransaction.addTransaction(request);
        return request;
    }
}
class GenerateDetails{
    Random random = new Random();
    int generateBillerId(){
        return (100000+random.nextInt(900000));
    }
    double generateBill(){
        return (100+random.nextDouble(900));
    }
    long generateTransferId(int bankId){
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(":", "");
        String main = String.valueOf(bankId) + time + String.valueOf((100000 + random.nextInt(900000)));
        return Long.parseLong(main);
    }
}
