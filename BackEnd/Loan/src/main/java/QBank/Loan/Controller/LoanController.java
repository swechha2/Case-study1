package QBank.Loan.Controller;

import QBank.Loan.Communication.DFeignRest;
import QBank.Loan.DTO.ApplyLoanRequest;
import QBank.Loan.Exception.InsufficientBalanceException;
import QBank.Loan.Exception.NoLoanFound;
import QBank.Loan.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    DFeignRest dFeignRest;

    @PostMapping("/apply")
    public ResponseEntity<Object> applyLoan(@RequestBody ApplyLoanRequest request){
        loanService.applyForLoan(request);
        return new ResponseEntity<>("Application Sent", HttpStatus.CREATED);
    }

    @GetMapping("/approved")
    public ResponseEntity<Object> approvedLoans(){
        return new ResponseEntity<>(loanService.getAllApprovedLoans(), HttpStatus.OK);
    }

    @GetMapping("/denied")
    public ResponseEntity<Object> deniedLoans(){
        return new ResponseEntity<>(loanService.getAllRejectedLoans(), HttpStatus.OK);
    }

    @GetMapping("/availableLoans")
    public ResponseEntity<Object> allAvailableLoans(){
        return new ResponseEntity<>(loanService.getAllLoanService(), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> test(){
        System.out.println("testing functionality");
        return new ResponseEntity<>(dFeignRest.getDetails(), HttpStatus.OK);
    }

    @PostMapping("/pay/{id}")
    public ResponseEntity<Object> payLoan(@PathVariable String id) throws NoLoanFound, InsufficientBalanceException {
        return new ResponseEntity<>(loanService.payLoan(id), HttpStatus.OK);
    }


}
