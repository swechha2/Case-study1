package QBank.LoginSignup.Controller;

//import QBank.LoginSignup.Config.JwtService;
import QBank.LoginSignup.Config.JwtTokenUtil;
import QBank.LoginSignup.DTO.*;
import QBank.LoginSignup.Entity.Branch;
import QBank.LoginSignup.Entity.Customer;
import QBank.LoginSignup.Exception.InvalidCredentialsException;
import QBank.LoginSignup.Exception.UserAlreadyExists;
import QBank.LoginSignup.Exception.UserNotFoundException;
import QBank.LoginSignup.Service.BranchService;
import QBank.LoginSignup.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class CustomerSignUpController {

    @Autowired
    CustomerService customerService;

    @Autowired
    BranchService branchService;

   /* @Autowired
    private AuthenticationManager authenticationManager;
*/
    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signUp(@RequestBody SignupRequest signup) throws UserAlreadyExists {
        //Customer customer = customerService.signUp(signup);
        return  new ResponseEntity<>(customerService.signUp(signup), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@RequestBody LoginRequest login) throws UserNotFoundException, InvalidCredentialsException {
        return new ResponseEntity<>(customerService.logIn(login), HttpStatus.OK);
    }


    @GetMapping("/sharedDetails")
    public ResponseEntity<SharedDetails> testing(){
        return new ResponseEntity<>(customerService.getDetails(), HttpStatus.OK);
    }


    @GetMapping("/test")
    public ResponseEntity<String> test(){
        System.out.println("Testing long");
        return new ResponseEntity<>("Test working", HttpStatus.OK);
    }

    @PostMapping("/addBranch")
    public ResponseEntity<Branch> addBranch(@RequestBody BranchRequest request){
        return new ResponseEntity<>(branchService.addBranch(request), HttpStatus.CREATED);
    }

    @GetMapping("/getAllBranch")
    public ResponseEntity<Object> getAllBranch(){
        return new ResponseEntity<>(branchService.getAllBranchs(), HttpStatus.OK);
    }

    @GetMapping("/getBranchByName/{name}")
    public ResponseEntity<Branch> getbyname(@PathVariable String name){
        return new ResponseEntity<>(branchService.findByBranchName(name), HttpStatus.OK);
    }

    @PutMapping("/updateAmount")
    public ResponseEntity<String> updateBalance(){
        customerService.updateBalance();
        return new ResponseEntity<>("Working", HttpStatus.OK);
    }

    @PutMapping("/deposit/{balance}")
    public ResponseEntity<Object> addMoney(@PathVariable double balance){
        return new ResponseEntity<>(customerService.DepositMoney(balance), HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public Customer getUser() throws UserNotFoundException {
        return customerService.getUser();
    }

    @PutMapping("/update")
    public void updateUser( @RequestBody  UpdateDetails updateDetails) throws UserNotFoundException {
        customerService.update(updateDetails);
    }



}
