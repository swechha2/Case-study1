package QBank.CreditCard.Service;

import QBank.CreditCard.Communication.DFeignSharedDetails;
import QBank.CreditCard.Communication.DFeignTransaction;
import QBank.CreditCard.Dto.ApplyRequest;
import QBank.CreditCard.Entity.CreditCard;
import QBank.CreditCard.Exception.CardNotFoundException;
import QBank.CreditCard.Exception.InsufficientFundsException;
import QBank.CreditCard.Repository.CreditCardRepository;
import QBank.LoginSignup.DTO.SharedDetails;
import QBank.Transactions.Dto.TransactionRequest;
import QBank.Transactions.Entity.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.smartcardio.Card;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CreditCardServiceImpl implements CreditCardService{

    GenerateDetails gd = new GenerateDetails();

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    DFeignSharedDetails sharedDetails;

    @Autowired
    DFeignTransaction dFeignTransaction;


    @Override
    public void applyCC(ApplyRequest request) {
        CreditCard cc = new CreditCard();
        Map<String, Object> details = gd.generateCard();
        cc.setCustomerId(sharedDetails.getDetails().getCustomerId());
        cc.setHolderName(request.getHolderName());
        cc.setCardType(request.getType());
        cc.setPayProvider(request.getPayProvider());

       // cc.setCreditCardId((Long) details.get("id"));
        cc.setCreditCardNumber((Long) details.get("number"));
        cc.setCvv((int) details.get("cvv"));
        cc.setExpiry((String) details.get("exp"));
        cc.setIssueDate((LocalDate) details.get("issue"));
        cc.setPin((int) details.get("pin"));

        cc.setStatus("approved");
        cc.setIsActive("active");
        cc.setCreditLimit(gd.generateLimit(request.getMaxLimit(), sharedDetails.getDetails().getScore()));

        creditCardRepository.save(cc);

    }

    @Override
    public List<CreditCard> getAllActiveCC() {
        return creditCardRepository.getActive();
    }

    @Override
    public List<CreditCard> getRejectCC() {
        return creditCardRepository.getReject();
    }

    @Override
    public double getCurrentDue()  {
        Random random = new Random();
        return random.nextDouble((100.0+random.nextDouble(90000.0)));
    }

    @Override
    public TransactionRequest payCCBill(long id) throws CardNotFoundException, InsufficientFundsException {
        //go to payment gateway
        CreditCard cc = creditCardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Not found try again"));
        TransactionRequest transactions = new TransactionRequest();
        transactions.setCustomerId(sharedDetails.getDetails().getCustomerId());
        transactions.setTransactionId(gd.generateTransferId(sharedDetails.getDetails().getBankId()));
        transactions.setAmount(getCurrentDue());
        transactions.setType("Credit Card");
        transactions.setDescription(cc.getHolderName() + String.valueOf(cc.getCreditCardNumber()) + "Credit card");
        transactions.setDate(LocalDate.now());
        transactions.setTime(LocalTime.now());
        transactions.setAccountNumber(cc.getCreditCardNumber());
        if(sharedDetails.getDetails().getBalance() < cc.getCurrentDue()){
            transactions.setStatus("Failed");
            dFeignTransaction.addTransaction(transactions);
            throw new InsufficientFundsException("too broke to pay this bill");
        }
        transactions.setStatus("Success");
        dFeignTransaction.addTransaction(transactions);
        System.out.println("paying bill");
        return transactions;
    }

    @Override
    public void cancelCC(long id) throws CardNotFoundException{
       CreditCard cc = creditCardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found"));
       cc.setIsActive("deactivated");
       creditCardRepository.save(cc);
    }
}
class GenerateDetails{
    private final int MAX_SCORE = 900;
    Random random = new Random();

    Map<String, Object> generateCard(){
       Map<String, Object> map = new HashMap<>();
      /* String samp =  date.toString().replace("-","");
       String exp = */
       map.put("number", (1000000000000000L + random.nextLong(9000000000000000L)));
       map.put("cvv", (100 + random.nextInt(900)));
       map.put("issue", LocalDate.now());
       map.put("exp", LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("MM/yyyy")));
       map.put("pin", (1000 + random.nextInt(9000)));
       map.put("id", (1000000000L + random.nextLong(9000000000L)));
       return map;
    }
    long generateTransferId(int bankId){
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(":", "");
        String main = String.valueOf(bankId) + time + String.valueOf((100000 + random.nextInt(900000)));
        return Long.parseLong(main);
    }
    double generateLimit(double maxAmount, double score){
        double per = (score/MAX_SCORE) * 100.0;
        return maxAmount * ( per /100.00);
    }

}
