package QBank.PaymentGateWay.Service;

import QBank.PaymentGateWay.Dto.LoginRequest;
import QBank.PaymentGateWay.Exception.InvalidPasswordException;
import QBank.PaymentGateWay.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;

public interface LoginService {

    void add(LoginRequest request);

    HttpStatus login(LoginRequest request) throws InvalidPasswordException, UserNotFoundException;
}
