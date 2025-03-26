package QBank.MoneyTransfer;

import QBank.MoneyTransfer.Communication.DFeignRest;
import QBank.MoneyTransfer.DTO.NomineeRequest;
import QBank.MoneyTransfer.DTO.NomineeTransferRequest;
import QBank.MoneyTransfer.DTO.TransferRequest;
import QBank.MoneyTransfer.Entity.Transfer;
import QBank.MoneyTransfer.Exceptions.NomineeDetailsNotFoundException;
import QBank.MoneyTransfer.Exceptions.NotEnoughBalanceException;
import QBank.MoneyTransfer.Service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControllerTest { ///  needs to rewrite to only test with static data
/// without reaching the controller
///
/// only method testing

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;

    @Mock
    private DFeignRest dFeignRest;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("Add Nominee Tests")
    class AddNomineeTests {

        @Test
        @DisplayName("Should successfully add nominee")
        void shouldAddNomineeSuccessfully() throws Exception {
            NomineeRequest request = new NomineeRequest();
            request.setName("John Doe");
            request.setRelRemarks("Brother");
            request.setAccountNumber(123456789L);
            request.setBankName("Test Bank");
            request.setBankId(1);
            request.setNickName("Johnny");

            doNothing().when(transferService).addNominee(any(NomineeRequest.class));

            mockMvc.perform(post("/api/v1/transfers/nominees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Nominee added successfully"));

            verify(transferService, times(1)).addNominee(any(NomineeRequest.class));
        }

        @Test
        @DisplayName("Should fail when nominee request is invalid")
        void shouldFailWithInvalidNomineeRequest() throws Exception {
            NomineeRequest request = new NomineeRequest();
            // Missing required fields

            mockMvc.perform(post("/api/v1/transfers/nominees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Transfer Without Nominee Tests")
    class TransferWithoutNomineeTests {

        @Test
        @DisplayName("Should successfully transfer money without nominee")
        void shouldTransferSuccessfullyWithoutNominee() throws Exception {
            TransferRequest request = new TransferRequest();
            request.setName("John Doe");
            request.setFromAccount(123456789L);
            request.setToAccount(987654321L);
            request.setAmount(1000.0);
            request.setRemarks("Test transfer");


            when(transferService.moneyTransferWithoutNominee(any(TransferRequest.class)))
                    .thenReturn(new Transfer());

            mockMvc.perform(post("/api/v1/transfers/direct")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Transfer successful"));
        }

        @Test
        @DisplayName("Should fail when balance is insufficient")
        void shouldFailWithInsufficientBalance() throws Exception {
            TransferRequest request = new TransferRequest();
            request.setName("John Doe");
            request.setFromAccount(123456789L);
            request.setToAccount(987654321L);
            request.setAmount(1000000.0);
            request.setRemarks("Test transfer");

            when(transferService.moneyTransferWithoutNominee(any(TransferRequest.class)))
                    .thenThrow(new NotEnoughBalanceException("Insufficient balance"));

            mockMvc.perform(post("/api/v1/transfers/direct")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isPaymentRequired());
        }
    }

    @Nested
    @DisplayName("Transfer With Nominee Tests")
    class TransferWithNomineeTests {

        @Test
        @DisplayName("Should successfully transfer money to nominee")
        void shouldTransferSuccessfullyToNominee() throws Exception {
            NomineeTransferRequest request = new NomineeTransferRequest();
            request.setNomineeName("John Doe");
            request.setAmount(1000.0);
            request.setAccountNumber(123456789L);
            request.setRemark("Test nominee transfer");

            when(transferService.moneyTransferWithNominee(any(NomineeTransferRequest.class)))
                    .thenReturn(new Transfer());

            mockMvc.perform(post("/api/v1/transfers/nominee-transfers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Transfer to nominee successful"));
        }

        @Test
        @DisplayName("Should fail when nominee not found")
        void shouldFailWhenNomineeNotFound() throws Exception {
            NomineeTransferRequest request = new NomineeTransferRequest();
            request.setNomineeName("Invalid Nominee");
            request.setAmount(1000.0);
            request.setAccountNumber(123456789L);
            request.setRemark("Test nominee transfer");

            when(transferService.moneyTransferWithNominee(any(NomineeTransferRequest.class)))
                    .thenThrow(new NomineeDetailsNotFoundException("Nominee not found"));

            mockMvc.perform(post("/api/v1/transfers/nominee-transfers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Balance Tests")
    class BalanceTests {

        @Test
        @DisplayName("Should successfully get balance")
        void shouldGetBalance() throws Exception {
            when(transferService.test()).thenReturn(5000.0);

            mockMvc.perform(get("/api/v1/transfers/balance"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("5000.0"));
        }

        @Test
        @DisplayName("Should successfully update balance")
        void shouldUpdateBalance() throws Exception {
            when(transferService.updateBalance()).thenReturn(6000.0);

            mockMvc.perform(get("/api/v1/transfers/balance/update"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("6000.0"));
        }
    }
@Test
@DisplayName("Should fail when nominee name is missing")
void shouldFailWhenNomineeNameIsMissing() throws Exception {
    NomineeRequest request = new NomineeRequest();
    request.setAccountNumber(123456789L);
    request.setBankName("Test Bank");
    request.setBankId(1);

    mockMvc.perform(post("/api/v1/transfers/nominees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Nominee name is required"));
}

@Test
@DisplayName("Should fail when bank ID is invalid")
void shouldFailWhenBankIdIsInvalid() throws Exception {
    NomineeRequest request = new NomineeRequest();
    request.setName("John Doe");
    request.setAccountNumber(123456789L);
    request.setBankName("Test Bank");
    request.setBankId(-1);

    mockMvc.perform(post("/api/v1/transfers/nominees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid bank ID"));
}

}