package QBank.LoginSignup.Service;


import QBank.LoginSignup.DTO.*;
import QBank.LoginSignup.Entity.Customer;
import QBank.LoginSignup.Exception.InvalidCredentialsException;
import QBank.LoginSignup.Exception.UserNotFoundException;

import java.util.Map;

public interface CustomerService {

    SignupResponse signUp(SignupRequest customer);

    //Customer testSignup(SignupRequest request);

    LoginResponse logIn(LoginRequest login) throws UserNotFoundException, InvalidCredentialsException;

    SharedDetails getDetails();

    void updateBalance();

    Map<String, Object> DepositMoney(double amount);

    Customer getUser() throws UserNotFoundException;

    void update(UpdateDetails customer) throws UserNotFoundException;


}
