package QBank.Transactions.Controller;

import QBank.Transactions.Dto.TransactionRequest;
import QBank.Transactions.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody TransactionRequest request){
        transactionService.addTransaction(request);
        return new ResponseEntity<>("Added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> all(){
        return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/between/{from}/{to}")
    public ResponseEntity<Object> between(@PathVariable LocalDate from, @PathVariable LocalDate to){
        return new ResponseEntity<>(transactionService.getBetweenDates(from, to), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Object> type(@PathVariable String type){
        return new ResponseEntity<>(transactionService.getByType(type), HttpStatus.OK);
    }
}