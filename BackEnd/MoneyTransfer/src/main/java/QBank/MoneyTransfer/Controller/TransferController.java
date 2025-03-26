package QBank.MoneyTransfer.Controller;

import QBank.MoneyTransfer.Communication.DFeignRest;
import QBank.MoneyTransfer.DTO.NomineeRequest;
import QBank.MoneyTransfer.DTO.NomineeTransferRequest;
import QBank.MoneyTransfer.DTO.TransferRequest;
import QBank.MoneyTransfer.Exceptions.NomineeDetailsNotFoundException;
import QBank.MoneyTransfer.Exceptions.NotEnoughBalanceException;
import QBank.MoneyTransfer.Service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    TransferService transferService;

    @Autowired
    DFeignRest dFeignRest;

    @PostMapping("/addNominee")
    public ResponseEntity<Object> addNominee(@RequestBody NomineeRequest request){
        transferService.addNominee(request);
        return new ResponseEntity<>("Add nominee", HttpStatus.CREATED);
    }

    @PostMapping("/transferWithoutNominee")
    public ResponseEntity<Object> withoutNominee(@RequestBody TransferRequest request)throws NotEnoughBalanceException {
        return new ResponseEntity<>(transferService.moneyTransferWithoutNominee(request), HttpStatus.OK);
    }

    @PostMapping("/transferWithNominee")
    public ResponseEntity<Object> withNominee(@RequestBody NomineeTransferRequest request)throws NotEnoughBalanceException, NomineeDetailsNotFoundException {
        return new ResponseEntity<>(transferService.moneyTransferWithNominee(request), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        System.out.println("Test successful");
        return new ResponseEntity<>("Test successful", HttpStatus.OK);
    }

    @GetMapping("/testing")
    public ResponseEntity<Object> testing(){
        return new ResponseEntity<>(dFeignRest.getDetails(), HttpStatus.OK);
    }

    @GetMapping("/updateBalance")
    public double updateBalance(){
        return transferService.updateBalance();
    }

    @GetMapping("/balance")
    public Object balance(){
        return transferService.test();
    }


}
