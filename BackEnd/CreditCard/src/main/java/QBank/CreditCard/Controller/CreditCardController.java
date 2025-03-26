package QBank.CreditCard.Controller;


import QBank.CreditCard.Communication.DFeignSharedDetails;
import QBank.CreditCard.Dto.ApplyRequest;
import QBank.CreditCard.Exception.CardNotFoundException;
import QBank.CreditCard.Exception.InsufficientFundsException;
import QBank.CreditCard.Service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creditCard")
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    DFeignSharedDetails dFeignRest;

    @PostMapping("/apply")
    public ResponseEntity<Object> apply(@RequestBody ApplyRequest request){
        creditCardService.applyCC(request);
        return new ResponseEntity<>("Applied", HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<Object> approved(){

        return new ResponseEntity<>(creditCardService.getAllActiveCC(), HttpStatus.OK);
    }

    @GetMapping("/reject")
    public ResponseEntity<Object> rejected(){

        return new ResponseEntity<>(creditCardService.getRejectCC(), HttpStatus.OK);
    }

    @PostMapping("/payBill/{id}")
    public ResponseEntity<Object> payBill(@PathVariable long id) throws CardNotFoundException, InsufficientFundsException {
        creditCardService.payCCBill(id);
        return new ResponseEntity<>("Paid", HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) throws CardNotFoundException{
        creditCardService.cancelCC(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/testing")
    public ResponseEntity<Object> test(){
        System.out.println("Test successful");
        return new ResponseEntity<>(dFeignRest.getDetails(), HttpStatus.OK);
    }
}
