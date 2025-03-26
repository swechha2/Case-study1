package QBank.PaymentGateWay.Service;

//import QBank.LoginSignup.DTO.SharedDetails;
import QBank.PaymentGateWay.Communication.DFeignRestSharedDetails;
import QBank.PaymentGateWay.Dto.LoginRequest;
import QBank.PaymentGateWay.Entity.PaymentGateWay;
import QBank.PaymentGateWay.Exception.InvalidPasswordException;
import QBank.PaymentGateWay.Exception.UserNotFoundException;
import QBank.PaymentGateWay.Repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LoginServiceImpl implements LoginService{


    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DFeignRestSharedDetails dFeignRestSharedDetails;

    @Override
    public HttpStatus login(LoginRequest request) throws InvalidPasswordException, UserNotFoundException {
        Optional<PaymentGateWay> login = loginRepository.findByUsername(request.getUsername());
        if(login.isEmpty()){
            System.out.println("logging in ");
            throw new UserNotFoundException("user not found");

        } else if(!passwordEncoder.matches(request.getPassword(), login.get().getPassword())){
            System.out.println("wrong password");
            throw new InvalidPasswordException("Password wrong");
        } else {
            System.out.println("login successful");
            return HttpStatus.OK;
        }
    }

    @Override
    public void add(LoginRequest request) {
        Random random = new Random();
        PaymentGateWay pgw = new PaymentGateWay();
        pgw.setCustomerId(dFeignRestSharedDetails.getDetails().getCustomerId());
        pgw.setUsername(request.getUsername());
        pgw.setPassword(passwordEncoder.encode(request.getPassword()));

        loginRepository.save(pgw);
    }
}
