package QBank.LoginSignup;

import QBank.LoginSignup.Communication.DFeignTransfers;
import QBank.LoginSignup.Config.JwtTokenUtil;
import QBank.LoginSignup.DTO.*;
import QBank.LoginSignup.Entity.Branch;
import QBank.LoginSignup.Entity.Customer;
import QBank.LoginSignup.Entity.Inlogs;
import QBank.LoginSignup.Exception.InvalidCredentialsException;
import QBank.LoginSignup.Exception.UserNotFoundException;
import QBank.LoginSignup.Repository.BranchRepository;
import QBank.LoginSignup.Repository.CustomerRepository;
import QBank.LoginSignup.Repository.InlogsRepository;
import QBank.LoginSignup.Service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private InlogsRepository inlogsRepository;

    @Mock
    private JwtTokenUtil jwtUtil;

    @Mock
    private DFeignTransfers dFeignTransfers;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private SignupRequest signupRequest;
    private Customer customer;
    private Branch branch;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setAge(30);
        signupRequest.setSex("Male");
        signupRequest.setAddress("123 Street");
        signupRequest.setPhone(1234567890L);
        signupRequest.setBranchName("Main Branch");
        signupRequest.setDeposit(1000.0);
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("password123");

        customer = new Customer();
        customer.setCustomerId(102412345);
        customer.setName("John Doe");
        customer.setPassword(passwordEncoder.encode("password123"));
        customer.setBankId(1);
        customer.setBalance(1000.0);

        branch = new Branch();
        branch.setBranchId(1);
        branch.setBankId(1);
        branch.setBranchName("Main Branch");
    }

    @Test
    void testSignUp_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(branchRepository.findByBranchName(anyString())).thenReturn(branch);
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        // Act
        SignupResponse response = customerService.signUp(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(signupRequest.getName(), response.getName());
        assertEquals(signupRequest.getEmail(), response.getEmail());
        assertTrue(response.getCustomerId() > 0);
        assertTrue(response.getAccountNumber() > 0);
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void testLogin_Success() throws UserNotFoundException, InvalidCredentialsException {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustId(102412345);
        loginRequest.setPassword("password123");
        //encode the password
        //passwordEncoder.encode(loginRequest.getPassword());
        //passwordEncoder.matches(loginRequest.getPassword(), encoded_password);

        when(customerRepo.findByCustomerId(loginRequest.getCustId())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("dummy-token");

        // Act
        LoginResponse response = customerService.logIn(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(customer.getCustomerId(), response.getCustomerId());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getToken());
        verify(inlogsRepository).save(any(Inlogs.class));
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustId(999999);
        loginRequest.setPassword("password123");

        when(customerRepo.findByCustomerId(loginRequest.getCustId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> customerService.logIn(loginRequest));
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustId(102412345);
        loginRequest.setPassword("wrongpassword");

        when(customerRepo.findByCustomerId(loginRequest.getCustId())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> customerService.logIn(loginRequest));
        verify(inlogsRepository).save(any(Inlogs.class));
    }

    @Test
    void testDepositMoney_Success() {
        // Arrange
        double depositAmount = 500.0;
        customer.setBalance(1000.0);
        SharedDetails sharedDetails = new SharedDetails();
        sharedDetails.setCustomerId(102412345);
        customerService.sharedDetails = sharedDetails;

        when(customerRepo.findByCustomerId(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        // Act
        Map<String, Object> response = customerService.DepositMoney(depositAmount);

        // Assert
        assertNotNull(response);
        assertEquals(1500.0, response.get("Balance"));
        assertEquals(102412345, response.get("Customer Id"));
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void testUpdateBalance_Success() {
        // Arrange
        SharedDetails sharedDetails = new SharedDetails();
        sharedDetails.setCustomerId(102412345);
        customerService.sharedDetails = sharedDetails;

        when(customerRepo.findByCustomerId(anyLong())).thenReturn(Optional.of(customer));
        when(dFeignTransfers.getBalance()).thenReturn(2000.0);

        // Act
        customerService.updateBalance();

        // Assert
        assertEquals(2000.0, customerService.getDetails().getBalance());
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void testGetUser_Success() throws UserNotFoundException {
        // Arrange
        SharedDetails sharedDetails = new SharedDetails();
        sharedDetails.setCustomerId(102412345);
        customerService.sharedDetails = sharedDetails;

        when(customerRepo.findByCustomerId(anyLong())).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.getUser();

        // Assert
        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
    }

    @Test
    void testGetUser_NotFound() {
        // Arrange
        SharedDetails sharedDetails = new SharedDetails();
        sharedDetails.setCustomerId(999999);
        customerService.sharedDetails = sharedDetails;

        when(customerRepo.findByCustomerId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> customerService.getUser());
    }

    @Test
    void testUpdate_Success() throws UserNotFoundException {
        // Arrange
        UpdateDetails updateDetails = new UpdateDetails();
        updateDetails.setName("Updated Name");
        updateDetails.setEmail("updated@example.com");
        updateDetails.setAge(35);
        updateDetails.setSex("Male");
        updateDetails.setAddress("456 Street");

        SharedDetails sharedDetails = new SharedDetails();
        sharedDetails.setCustomerId(102412345);
        customerService.sharedDetails = sharedDetails;

        when(customerRepo.findByCustomerId(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        // Act
        customerService.update(updateDetails);

        // Assert
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void testGenerateCScore() {
        // Test different balance ranges
        assertTrue(customerService.generateCScore(0.5) >= 400 && customerService.generateCScore(0.5) < 700);
        assertTrue(customerService.generateCScore(50.0) >= 500 && customerService.generateCScore(50.0) < 750);
        assertTrue(customerService.generateCScore(500.0) >= 600 && customerService.generateCScore(500.0) < 820);
        assertTrue(customerService.generateCScore(2000.0) >= 700 && customerService.generateCScore(2000.0) < 870);
        assertTrue(customerService.generateCScore(-1.0) >= 100 && customerService.generateCScore(-1.0) < 600);
    }

    @Test
    void testGetBankId_Success() {
        // Arrange
        when(branchRepository.findByBranchName("Main Branch")).thenReturn(branch);

        // Act
        int bankId = customerService.getBankId("Main Branch");

        // Assert
        assertEquals(1, bankId);
        verify(branchRepository).findByBranchName("Main Branch");
    }
}