package QBank.Loan.Service;

import QBank.Loan.Communication.DFeignRest;
import QBank.Loan.Communication.DFeignTransaction;
import QBank.Loan.DTO.ApplyLoanRequest;
import QBank.Loan.Entity.AvailableLoans;
import QBank.Loan.Entity.Loan;
import QBank.Loan.Exception.InsufficientBalanceException;
import QBank.Loan.Exception.NoLoanFound;
import QBank.Loan.Repository.AvailableLoansRepository;
import QBank.Loan.Repository.LoanRepository;
import QBank.LoginSignup.DTO.SharedDetails;
import QBank.Transactions.Dto.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class LoanServiceImpl implements LoanService{

    GenerateDetails gd = new GenerateDetails();

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AvailableLoansRepository availableLoansRepository;

    @Autowired
    DFeignRest dFeignRest;

    @Autowired
    DFeignTransaction dFeignTransaction;


    @Override
    public void applyForLoan(ApplyLoanRequest request) {
        Loan loan = new Loan();
        AvailableLoans loanD = availableLoansRepository.findByLoanType(request.getLoanType());

        loan.setCustLoanId(gd.generateCustLoanId(loanD));
        loan.setCustomerId(dFeignRest.getDetails().getCustomerId());                        ///To be set later by using customer detials from the customer table
        loan.setAccountNumber(dFeignRest.getDetails().getAccountNumber());                      ///To be set later by using customer detials from the customer table
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenure(request.getTenure());
        loan.setFrequency(request.getFrequency());
        loan.setDate(LocalDate.now());
        loan.setLoanId(loanD.getLoanId());
        boolean status = gd.approval(loan, loanD.getMaxLoanAmount(), dFeignRest.getDetails().getScore());
        if(status) {
            loan.setApproval("Approved");
            loan.setOngoing(true);
            loan.setDueDate(LocalDate.now().plusDays(40));
            loan.setTotalPayable(request.getLoanAmount() + (request.getLoanAmount() + loanD.getInterestRate()/100 * request.getTenure()));
            loan.setNextPayable(loan.getTotalPayable()/loan.getTenure());
        }

        else {
            loan.setApproval("Denied");
            loan.setOngoing(false);
        }
        loanRepository.save(loan);
    }

    @Override
    public List<Loan> getAllApprovedLoans() {
        return loanRepository.getAllApprovedLoans();
    }

    @Override
    public List<Loan> getAllRejectedLoans() {
        return loanRepository.getAllRejectLoans();
    }

    @Override
    public List<AvailableLoans> getAllLoanService() {
        return availableLoansRepository.findAll();
    }

    @Override
    public Map<String, Object> payLoan(String id) throws NoLoanFound, InsufficientBalanceException {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new NoLoanFound("Loan not found, check your Id"));
        Map<String, Object> res = gd.dueAmount(loan.getFrequency(), loan.getDueDate(),loan.getLoanAmount()*0.2,loan.getLoanAmount(), loan.getTenure());
        Map<String, Object> r = new HashMap<>();
        loan.setLoanAmount((Double) res.get("amount"));
        loan.setDueDate((LocalDate) res.get("nextDue"));
        loan.setNextPayable((Double) res.get("nextPay"));
        loan.setTenure((Integer) res.get("tenure"));
        loan.setOngoing((Boolean) res.get("ongoing"));

        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(dFeignRest.getDetails().getCustomerId());
        request.setAccountNumber(loan.getLoanId());
        request.setType("Loan");
        request.setTime(LocalTime.now());
        request.setDate(LocalDate.now());
        request.setDescription(loan.getCustLoanId() + "Loan");
        request.setAmount(loan.getNextPayable());
        request.setTransactionId(gd.generateTransferId(dFeignRest.getDetails().getBankId()));
        if(dFeignRest.getDetails().getBalance() < (double) res.get("nextPay")) {
            request.setStatus("Failed");
            dFeignTransaction.addTransaction(request);
            throw new InsufficientBalanceException("Too broke to pay this bill");
        }
        request.setStatus("success");
        dFeignTransaction.addTransaction(request);
        r.put("Payment","Success");
        r.put("Id", loan.getLoanId());
        r.put("Customer Id", dFeignRest.getDetails().getCustomerId());
        return r;
    }
}
class GenerateDetails{
    private final int MAX_C_SCORE = 900;
    Random random = new Random();

    String generateCustLoanId(AvailableLoans loan){
        Random random = new Random();
        return String.valueOf(loan.getLoanId()) + loan.getLoanType() + String.valueOf((100000 + random.nextInt(900000)));
    }
    boolean approval(Loan loan, double maxAmount, double score){
        double amountPercent = (loan.getLoanAmount()/maxAmount) * 100;
        double scorePercent = (score/MAX_C_SCORE) * 100; //799 later to be replaced to by the cbill score got from customer table;
        return amountPercent >= scorePercent;
    }
    long generateTransferId(int bankId){
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(":", "");
        String main = String.valueOf(bankId) + time + String.valueOf((100000 + random.nextInt(900000)));
        return Long.parseLong(main);
    }
    Map<String, Object> dueAmount(String frequency, LocalDate date, double add, double amount, int tenure){
        double topay = 0;
        Map<String, Object> map = new HashMap<>();
        LocalDate nextDue = null;
        if(frequency.matches("monthly") && LocalDate.now().isAfter(date)){
            if(Duration.between(date, LocalDate.now()).toDays() > 40){
                 topay = amount/tenure + add;
                 tenure-=1;
                 long days = Duration.between(date, LocalDate.now()).toDays();
                 amount-= topay;
                 nextDue = LocalDate.now().plusDays(20 +(days-40));
                map.put("amount", amount);
                map.put("nextDue", nextDue);
                map.put("nextPay", topay);
                map.put("tenure", tenure);
                map.put("ongoing", true);
                return map;
            }else if((Duration.between(date, LocalDate.now()).toDays() < 40) && (Duration.between(date, LocalDate.now()).toDays() > 31)){
                 topay = amount/tenure;
                 tenure-=1;
                nextDue = LocalDate.now().plusDays(40);
                amount-= topay + add;
                map.put("amount", amount);
                map.put("nextDue", nextDue);
                map.put("nextPay", topay);
                map.put("tenure", tenure);
                map.put("ongoing", true);
                return map;
            }

        }else if(frequency.matches("once")){
            map.put("amount", amount);
            map.put("nextDue", null);
            map.put("nextPay", null);
            map.put("tenure", null);
            map.put("ongoing", false);
            return map;
        } else if(amount<1){
            map.put("amount", 0.000);
            map.put("nextDue", null);
            map.put("nextPay", null);
            map.put("tenure", null);
            map.put("ongoing", false);
            return map;
        }
        map.put("amount", 0.000);
        map.put("nextDue", null);
        map.put("nextPay", null);
        map.put("tenure", null);
        map.put("ongoing", false);
        return map;
    }
}
