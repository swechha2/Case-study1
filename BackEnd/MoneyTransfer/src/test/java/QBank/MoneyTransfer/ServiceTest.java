package QBank.MoneyTransfer;

import QBank.LoginSignup.DTO.SharedDetails;
import QBank.MoneyTransfer.Communication.DFeignRest;
import QBank.MoneyTransfer.Communication.DFeignTransaction;
import QBank.MoneyTransfer.DTO.NomineeRequest;
import QBank.MoneyTransfer.DTO.NomineeTransferRequest;
import QBank.MoneyTransfer.DTO.TransferRequest;
import QBank.MoneyTransfer.Entity.Nominee;
import QBank.MoneyTransfer.Entity.Transfer;
import QBank.MoneyTransfer.Exceptions.NomineeDetailsNotFoundException;
import QBank.MoneyTransfer.Exceptions.NotEnoughBalanceException;
import QBank.MoneyTransfer.Repository.NomineeRepository;
import QBank.MoneyTransfer.Repository.TransferRepository;
import QBank.MoneyTransfer.Service.GenerateDetails;
import QBank.MoneyTransfer.Service.TransferServiceImpl;
import QBank.Transactions.Dto.TransactionRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private NomineeRepository nomineeRepository;

    @Mock
    private DFeignRest dFeignRest;

    @Mock
    private DFeignTransaction dFeignTransaction;

    private SharedDetails mockCustomerDetails;


    @BeforeEach
    void setUp() {
        mockCustomerDetails = new SharedDetails();
        mockCustomerDetails.setCustomerId(12L);
        mockCustomerDetails.setName("John Doe");
        mockCustomerDetails.setAccountNumber(123456789L);
        mockCustomerDetails.setBalance(10000.0);
        mockCustomerDetails.setBankId(1);

        when(dFeignRest.getDetails()).thenReturn(mockCustomerDetails);
    }

    @Nested
    @DisplayName("Add Nominee Tests")
    class AddNomineeTests {

        @Test
        @DisplayName("Should successfully add a nominee")
        void shouldAddNomineeSuccessfully() {
            // Arrange
            NomineeRequest request = new NomineeRequest();
            request.setName("Jane Doe");
            request.setAccountNumber(987654321L);
            request.setBankId(1);
            request.setBankName("Test Bank");
            request.setNickName("Jane");
            request.setRelRemarks("Sister");

            ArgumentCaptor<Nominee> nomineeCaptor = ArgumentCaptor.forClass(Nominee.class);

            // Act
            transferService.addNominee(request);

            // Assert
            verify(nomineeRepository).save(nomineeCaptor.capture());
            Nominee savedNominee = nomineeCaptor.getValue();
            assertAll(
                    "Verify nominee details",
                     () -> assertEquals(request.getName(), savedNominee.getName()),
                    () -> assertEquals(request.getAccountNumber(), savedNominee.getAccountNumber()),
                    () -> assertEquals(request.getBankId(), savedNominee.getBankId()),
                    () -> assertNotNull(savedNominee.getNomineeId())
            );
        }
    }

    @Nested
    @DisplayName("Money Transfer Without Nominee Tests")
    class MoneyTransferWithoutNomineeTests {

        @Test
        @DisplayName("Should successfully transfer money when balance is sufficient")
        void shouldTransferMoneySuccessfully() throws NotEnoughBalanceException {
            // Arrange
            TransferRequest request = new TransferRequest();
            request.setName("Test Transfer");
            request.setToAccount(987654321L);
            request.setAmount(5000.0);
            request.setRemarks("Test transfer");

            // Act
            Transfer result = transferService.moneyTransferWithoutNominee(request);

            // Assert
            assertAll(
                    "Verify transfer details",
                    () -> assertEquals("Success", result.getStatus()),
                    () -> assertEquals(request.getAmount(), result.getAmount()),
                    () -> assertEquals(request.getToAccount(), result.getToAccount()),
                    () -> assertEquals(mockCustomerDetails.getAccountNumber(), result.getFromAccount()),
                    () -> verify(transferRepository).save(any(Transfer.class)),
                    () -> verify(dFeignTransaction).addTransaction(any(TransactionRequest.class))
            );
        }

        @Test
        @DisplayName("Should throw exception when balance is insufficient")
        void shouldThrowExceptionWhenInsufficientBalance() {
            // Arrange
            TransferRequest request = new TransferRequest();
            request.setName("Test Transfer");
            request.setToAccount(987654321L);
            request.setAmount(15000.0); // More than available balance
            request.setRemarks("Test transfer");

            // Act & Assert
            NotEnoughBalanceException exception = assertThrows(
                    NotEnoughBalanceException.class,
                    () -> transferService.moneyTransferWithoutNominee(request)
            );
            assertEquals("Insufficient Balance. Add funds and try again", exception.getMessage());

            // Verify failed transfer is saved
            verify(transferRepository).save(argThat(transfer ->
                    "Failed".equals(transfer.getStatus())
            ));
        }
    }

    @Nested
    @DisplayName("Money Transfer With Nominee Tests")
    class MoneyTransferWithNomineeTests {

        @Test
        @DisplayName("Should successfully transfer money to nominee")
        void shouldTransferMoneyToNomineeSuccessfully() throws NotEnoughBalanceException, NomineeDetailsNotFoundException {
            // Arrange
            NomineeTransferRequest request = new NomineeTransferRequest();
            request.setNomineeName("Jane Doe");
            request.setAccountNumber(123456789L);
            request.setAmount(5000.0);
            request.setRemark("Test nominee transfer");

            Nominee nominee = new Nominee();
            nominee.setName("Jane Doe");
            nominee.setAccountNumber(987654321L);
            nominee.setBankId(1);

            when(nomineeRepository.findByName(request.getNomineeName())).thenReturn(nominee);

            // Act
            Transfer result = transferService.moneyTransferWithNominee(request);

            // Assert
            assertAll(
                    "Verify nominee transfer details",
                    () -> assertEquals("Success", result.getStatus()),
                    () -> assertEquals(request.getAmount(), result.getAmount()),
                    () -> assertEquals(nominee.getAccountNumber(), result.getToAccount()),
                    () -> assertEquals(request.getAccountNumber(), result.getFromAccount()),
                    () -> verify(transferRepository).save(any(Transfer.class)),
                    () -> verify(dFeignTransaction).addTransaction(any(TransactionRequest.class))
            );
        }

        @Test
        @DisplayName("Should throw exception when nominee not found")
        void shouldThrowExceptionWhenNomineeNotFound() {
            // Arrange
            NomineeTransferRequest request = new NomineeTransferRequest();
            request.setNomineeName("Unknown Nominee");
            request.setAmount(5000.0);

            when(nomineeRepository.findByName(request.getNomineeName())).thenReturn(null);

            // Act & Assert
            NomineeDetailsNotFoundException exception = assertThrows(
                    NomineeDetailsNotFoundException.class,
                    () -> transferService.moneyTransferWithNominee(request)
            );
            assertEquals("Nominee not found/nAdd a new nominee", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when insufficient balance for nominee transfer")
        void shouldThrowExceptionWhenInsufficientBalanceForNomineeTransfer() {
            // Arrange
            NomineeTransferRequest request = new NomineeTransferRequest();
            request.setNomineeName("Jane Doe");
            request.setAmount(15000.0); // More than available balance

            Nominee nominee = new Nominee();
            nominee.setName("Jane Doe");
            nominee.setAccountNumber(987654321L);
            nominee.setBankId(1);

            when(nomineeRepository.findByName(request.getNomineeName())).thenReturn(nominee);

            // Act & Assert
            assertThrows(NotEnoughBalanceException.class,
                    () -> transferService.moneyTransferWithNominee(request));
        }
    }

    @Nested
    @DisplayName("Balance Management Tests")
    class BalanceManagementTests {

        @Test
        @DisplayName("Should return correct updated balance")
        void shouldReturnCorrectUpdatedBalance() {
            // Arrange
            double expectedBalance = 10000.0;

            // Act
            double actualBalance = transferService.updateBalance();

            // Assert
            assertEquals(expectedBalance, actualBalance);
        }

        @Test
        @DisplayName("Should save and return correct transfer details")
        void shouldSaveAndReturnTransferDetails() {
            // Act
            Map<String, Object> result = transferService.saveTransfer();

            // Assert
            assertAll(
                    "Verify transfer details map",
                    () -> assertEquals(mockCustomerDetails.getCustomerId(), result.get("feild 1")),
                    () -> assertEquals(mockCustomerDetails.getName(), result.get("feild 2")),
                    () -> assertEquals(mockCustomerDetails.getAccountNumber(), result.get("feild 3")),
                    () -> assertEquals(mockCustomerDetails.getBalance(), result.get("feild 4"))
            );
        }
    }

    @Nested
    @DisplayName("Generate Details Tests")
    class GenerateDetailsTests {

        @Test
        @DisplayName("Should generate valid nominee ID")
        void shouldGenerateValidNomineeId() {
            // Arrange
            GenerateDetails generateDetails = new GenerateDetails();

            // Act
            int nomineeId = generateDetails.generateNomineeId();

            // Assert
            assertTrue(String.valueOf(nomineeId).length() >= 6);
        }

        @Test
        @DisplayName("Should generate valid transfer ID")
        void shouldGenerateValidTransferId() {
            // Arrange
            GenerateDetails generateDetails = new GenerateDetails();
            int bankId = 1;

            // Act
            long transferId = generateDetails.generateTransferId(bankId);

            // Assert
            assertTrue(String.valueOf(transferId).startsWith(String.valueOf(bankId)));
            assertTrue(String.valueOf(transferId).length() >= 13);
        }
    }
}