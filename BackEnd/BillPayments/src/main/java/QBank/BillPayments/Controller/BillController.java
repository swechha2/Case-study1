package QBank.BillPayments.Controller;

import QBank.BillPayments.Communication.DFeignRest;
import QBank.BillPayments.DTO.BillRequest;
import QBank.BillPayments.Exception.BillNotFoundException;
import QBank.BillPayments.Service.BillsService;
import QBank.CreditCard.Exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    BillsService billsService;

    @Autowired
    DFeignRest dFeignRest;

    @PostMapping("/addBill")
    public ResponseEntity<Object> addBill(@RequestBody BillRequest request){
        billsService.addBill(request);
        return new ResponseEntity<>("Added Bill", HttpStatus.CREATED);
    }

    @GetMapping("getAll")
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(billsService.getAllBills(), HttpStatus.OK);
    }


    @PostMapping("/payBill/{id}")
    public ResponseEntity<Object> payBill(@PathVariable int id) throws BillNotFoundException, InsufficientFundsException {
        return new ResponseEntity<>(billsService.payBill(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteBill")
    public ResponseEntity<Object> deleteBill(@PathVariable int id) throws BillNotFoundException {
        billsService.deleteBill(id);
        return new ResponseEntity<>("Bill deleted", HttpStatus.OK
        );
    }

    @GetMapping("/test")
    public ResponseEntity<Object> test(){
        return new ResponseEntity<>(dFeignRest.getDetails(), HttpStatus.OK);
    }
}
