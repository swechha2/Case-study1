package QBank.PaymentGateWay.Controller;

import QBank.PaymentGateWay.Communication.DFeignRestSharedDetails;
import QBank.PaymentGateWay.Dto.LoginRequest;
import QBank.PaymentGateWay.Exception.InvalidPasswordException;
import QBank.PaymentGateWay.Exception.UserNotFoundException;
import QBank.PaymentGateWay.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentGateway")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    DFeignRestSharedDetails dFeignRestSharedDetails;

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) throws UserNotFoundException, InvalidPasswordException {
        return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
    }

    @PostMapping("/config")
    public ResponseEntity<Object> add(@RequestBody LoginRequest request){
        loginService.add(request);
        return new ResponseEntity<>("added info", HttpStatus.CREATED);
    }

    @GetMapping("/testing")
    public ResponseEntity<Object> testing(){
        return new ResponseEntity<>(dFeignRestSharedDetails.getDetails(), HttpStatus.OK);
    }
}
