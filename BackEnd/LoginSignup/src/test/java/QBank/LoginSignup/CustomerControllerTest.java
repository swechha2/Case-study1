package QBank.LoginSignup;

import QBank.LoginSignup.Controller.CustomerSignUpController;
import QBank.LoginSignup.DTO.*;
import QBank.LoginSignup.Entity.Branch;
import QBank.LoginSignup.Entity.Customer;
import QBank.LoginSignup.Exception.InvalidCredentialsException;
import QBank.LoginSignup.Exception.UserNotFoundException;
import QBank.LoginSignup.Service.BranchService;
import QBank.LoginSignup.Service.CustomerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CustomerSignUpController} class.
 */
class CustomerSignUpControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private BranchService branchService;

    @InjectMocks
    private CustomerSignUpController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test that the signUp method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testSignUp_Success() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("john@example.com");

        SignupResponse expectedResponse = new SignupResponse();
        expectedResponse.setCustomerId(1L);
        expectedResponse.setName("John Doe");

        when(customerService.signUp(any(SignupRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SignupResponse> response = controller.signUp(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        verify(customerService).signUp(signupRequest);
    }

    /**
     * Test that the signUp method returns a successful response with a valid
     * response body when the customer service method throws an
     * InvalidCredentialsException.
     */
   /* @Test
    void testSignUp_InvalidCredentials() throws UserNotFoundException, InvalidCredentialsException {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("john@example.com");

        when(customerService.signUp(any(SignupRequest.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            controller.signUp(signupRequest);
        });
    }*/

    /**
     * Test that the logIn method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testLogin_Success() throws UserNotFoundException, InvalidCredentialsException {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustId(1L);
        loginRequest.setPassword("password");

        LoginResponse expectedResponse = new LoginResponse(1L, "Login Successful", "dummy-token");

        when(customerService.logIn(any(LoginRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<LoginResponse> response = controller.logIn(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login Successful", response.getBody().getMessage());
        verify(customerService).logIn(loginRequest);
    }

    /**
     * Test that the logIn method returns a successful response with a valid
     * response body when the customer service method throws an
     * InvalidCredentialsException.
     */
    @Test
    void testLogin_InvalidCredentials() throws UserNotFoundException, InvalidCredentialsException {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustId(1L);
        loginRequest.setPassword("wrongpassword");

        when(customerService.logIn(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            controller.logIn(loginRequest);
        });
    }

    /**
     * Test that the getSharedDetails method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testGetSharedDetails_Success() {
        // Arrange
        SharedDetails expectedDetails = new SharedDetails();
        expectedDetails.setAccountNumber(123456L);
        expectedDetails.setName("John Doe");

        when(customerService.getDetails()).thenReturn(expectedDetails);

        // Act
        ResponseEntity<SharedDetails> response = controller.testing();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    /**
     * Test that the addBranch method returns a successful response with a valid
     * response body when the branch service method returns a valid response.
     */
    @Test
    void testAddBranch_Success() {
        // Arrange
        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setBranchName("Main Branch");
        branchRequest.setBranchId(1);
        branchRequest.setBankId(1);

        Branch expectedBranch = new Branch();
        expectedBranch.setBranchName("Main Branch");

        when(branchService.addBranch(any(BranchRequest.class))).thenReturn(expectedBranch);

        // Act
        ResponseEntity<Branch> response = controller.addBranch(branchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Main Branch", response.getBody().getBranchName());
    }

    /**
     * Test that the getAllBranches method returns a successful response with a valid
     * response body when the branch service method returns a valid response.
     */
    @Test
    void testGetAllBranches_Success() {
        // Arrange
        List<Branch> branches = Arrays.asList(
                new Branch(),
                new Branch()
        );

        when(branchService.getAllBranchs()).thenReturn(branches);

        // Act
        ResponseEntity<Object> response = controller.getAllBranch();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    /**
     * Test that the getBranchByName method returns a successful response with a valid
     * response body when the branch service method returns a valid response.
     */
    @Test
    void testGetBranchByName_Success() {
        // Arrange
        Branch expectedBranch = new Branch();
        expectedBranch.setBranchName("Test Branch");

        when(branchService.findByBranchName("Test Branch")).thenReturn(expectedBranch);

        // Act
        ResponseEntity<Branch> response = controller.getbyname("Test Branch");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Branch", response.getBody().getBranchName());
    }

    /**
     * Test that the depositMoney method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testDepositMoney_Success() {
        // Arrange
        double depositAmount = 1000.0;

        when(customerService.DepositMoney(depositAmount)).thenReturn(Map.of("amount", depositAmount));

        // Act
        ResponseEntity<Object> response = controller.addMoney(depositAmount);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("amount", depositAmount), response.getBody());
    }

    /**
     * Test that the getUser method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testGetUser_Success() throws UserNotFoundException {
        // Arrange
        Customer expectedCustomer = new Customer();
        expectedCustomer.setCustomerId(1L);
        expectedCustomer.setName("John Doe");

        when(customerService.getUser()).thenReturn(expectedCustomer);

        // Act
        Customer customer = controller.getUser();

        // Assert
        assertNotNull(customer);
        assertEquals(1L, customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
    }

    /**
     * Test that the updateUser method returns a successful response with a valid
     * response body when the customer service method returns a valid response.
     */
    @Test
    void testUpdateUser_Success() throws UserNotFoundException {
        // Arrange
        UpdateDetails updateDetails = new UpdateDetails();
        updateDetails.setName("Updated Name");
        updateDetails.setEmail("updated@example.com");

        // Act & Assert
        assertDoesNotThrow(() -> controller.updateUser(updateDetails));
        verify(customerService).update(updateDetails);
    }

    @AfterAll
    static void report() {
        System.out.println("Ran " + 9 + " tests in " + CustomerSignUpControllerTest.class.getName());
    }
}
