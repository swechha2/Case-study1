package QBank.LoginSignup.Service;

import QBank.LoginSignup.Communication.DFeignTransfers;
import QBank.LoginSignup.Config.JwtTokenUtil;
import QBank.LoginSignup.DTO.*;
import QBank.LoginSignup.Entity.Branch;
import QBank.LoginSignup.Entity.Customer;
import QBank.LoginSignup.Entity.Inlogs;
import QBank.LoginSignup.Exception.InvalidCredentialsException;
import QBank.LoginSignup.Exception.UserAlreadyExists;
import QBank.LoginSignup.Exception.UserNotFoundException;
import QBank.LoginSignup.Repository.BranchRepository;
import QBank.LoginSignup.Repository.CustomerRepository;
import QBank.LoginSignup.Repository.InlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


@Service
public class CustomerServiceImpl implements CustomerService{
    public String baseCode = "1024";
    Random random = new Random();
    public SharedDetails sharedDetails = new SharedDetails();

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    InlogsRepository inlogsRepository;

    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    DFeignTransfers dFeignTransfers;


    @Override
    public SignupResponse signUp(SignupRequest customer)  throws UserAlreadyExists {
        Customer cs = customerRepo.findByEmail(customer.getEmail());
        if(cs != null){
            throw new UserAlreadyExists("User already exists with email: " + customer.getEmail());
        }
        SignupResponse response = new SignupResponse();
        Customer customer1 = new Customer();
        customer1.setName(customer.getName());
        customer1.setAge((customer.getAge()));
        customer1.setSex(customer.getSex());
        customer1.setAddress(customer.getAddress());
        customer1.setPhone(customer.getPhone());
        customer1.setBranchName(customer.getBranchName());
        customer1.setBalance(customer.getDeposit());
        customer1.setEmail(customer.getEmail());
        customer1.setRole("User");
        customer1.setScore(generateCScore(customer.getDeposit()));
        //customer1.setBalance(0.00);
        customer1.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer1.setCustomerId(generateCustomerId());
        customer1.setAccountNumber(generateAccountNumber(customer1.getBranchName()));
        customer1.setBankId(getBankId(customer.getBranchName()));

        response.setCustomerId(customer1.getCustomerId());
        response.setAccountNumber(customer1.getAccountNumber());
        response.setName(customer1.getName());
        response.setEmail(customer1.getEmail());
        response.setBankId(customer1.getBankId());
        response.setBalance(customer1.getBalance());
        response.setScore(customer1.getScore());

        customerRepo.save(customer1);
        return response;

    }

    @Override
    public LoginResponse logIn(LoginRequest login) throws UserNotFoundException, InvalidCredentialsException {
        Inlogs inlogs = new Inlogs();
        LoginResponse response = new LoginResponse();
        inlogs.setDate(LocalDate.now());
        inlogs.setTime(LocalTime.now());
        //inlogs.setState("Successful");
        //System.out.println(login.getUsername());
        Customer customer = customerRepo.findByEmail(login.getUsername());


        if(customer == null){
            throw new UserNotFoundException("No User found ");
        } else if(!passwordEncoder.matches(login.getPassword(), customer.getPassword())){
            inlogs.setState("Login failed, Invalid password");
            inlogsRepository.save(inlogs);
            throw new InvalidCredentialsException("Invalid Username or Password");
        }
        System.out.println("loginSuccessful");
        sharedDetails.setCustomerId(customer.getCustomerId());
        sharedDetails.setAccountNumber(customer.getAccountNumber());
        sharedDetails.setName(customer.getName());
        sharedDetails.setBalance(customer.getBalance());
        sharedDetails.setScore(customer.getScore());
        sharedDetails.setBankId(customer.getBankId());
        inlogs.setState("Successful");
        inlogsRepository.save(inlogs);
        response.setCustomerId( customer.getCustomerId());
        response.setMessage( "Login successful");
        response.setToken(jwtUtil.generateToken(String.valueOf(customer.getCustomerId())));
        return response;
    }

    @Override
    public SharedDetails getDetails() {
        return sharedDetails;
    }
    @Override
    public void updateBalance() {
        Customer customer = customerRepo.findByCustomerId(sharedDetails.getCustomerId()).orElse(null);
        if(customer !=null) {
            customer.setBalance(dFeignTransfers.getBalance());
            customerRepo.save(customer);
            sharedDetails.setBalance(dFeignTransfers.getBalance());
        }
    }

    @Override
    public Map<String, Object> DepositMoney(double amount) {
        Customer customer = customerRepo.findByCustomerId(sharedDetails.getCustomerId()).orElse(null);
        if(customer != null){

            customer.setBalance((customer.getBalance()+amount));
            customerRepo.save(customer);
        }
        sharedDetails.setBalance(customer.getBalance());
        Map<String, Object> response = new HashMap<>();
        response.put("Customer Id", customer.getCustomerId());
        response.put("Balance", customer.getBalance());
        return  response;
    }

    @Override
    public Customer getUser() throws UserNotFoundException {
        return customerRepo.findByCustomerId(sharedDetails.getCustomerId()).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Override
    public void update(UpdateDetails customer) throws UserNotFoundException {
        Customer customer1 = getUser();
        customer1.setName(customer.getName());
        customer1.setAddress(customer.getAddress());
        customer1.setAge(customer.getAge());
        customer1.setSex(customer.getSex());
        customer1.setEmail(customer.getEmail());
        customerRepo.save(customer1);
    }

    public long generateAccountNumber(String branchName){
        LocalDate date = LocalDate.now();
        Branch branch = branchRepository.findByBranchName(branchName);
        String branchId = String.valueOf(branch.getBranchId());
        String num = baseCode + String.valueOf(1024) + String.valueOf(date.getYear()) + String.valueOf((100000+random.nextInt(900000)));
        return Long.parseLong(num);
    }

    public int generateCustomerId(){
        String finalCode = baseCode + String.valueOf((100000+random.nextInt(900000)));
        return Integer.parseInt(finalCode);
    }

    public int getBankId(String branchName){
        Branch branch = branchRepository.findByBranchName(branchName);
        return branch.getBankId();
        //return
    }

    public int generateCScore(Double Balance) {
        //int c_Score = 0;
        if (Balance < 1.00) {
            return (400 + random.nextInt(300));
        } else if (Balance < 100.00 && Balance > 10.00) {
            return (500 + random.nextInt(250));
        } else if (Balance > 100.00 && Balance < 1000.00) {
            return (600  + random.nextInt(220));
        } else if(Balance > 1000.00){
            return (700  + random.nextInt(170));
        } else {
            return (100 + random.nextInt(500));
        }
    }


}

