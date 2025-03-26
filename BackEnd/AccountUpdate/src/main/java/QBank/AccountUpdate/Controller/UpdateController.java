package QBank.AccountUpdate.Controller;


import QBank.AccountUpdate.Communication.DFeingUpdate;
import QBank.AccountUpdate.DTO.UpdateRequest;
import QBank.AccountUpdate.Service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    UpdateService updateService;

    @Autowired
    DFeingUpdate dFeingUpdate;

    @PutMapping("/updateUser")
    public ResponseEntity<Object> updateAccount(@RequestBody UpdateRequest request){
        return new ResponseEntity<>(updateService.updatedetails(request), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> get(){
        return new ResponseEntity<>(dFeingUpdate.getCustomer(), HttpStatus.OK);
    }
}
