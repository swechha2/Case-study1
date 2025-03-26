package QBank.MoneyTransfer.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handelGlobalException(Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "An Unexpected error occurred");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidNomineeDetails.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDetailsException(InvalidNomineeDetails ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Invalid Nominee details, try again");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NomineeDetailsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNomineeDetailsNotFoundException(NomineeDetailsNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Details not found, try again");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleNotEnoughBalanceException(NotEnoughBalanceException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Payment Failed");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentFailedException(PaymentFailedException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Payment Failed");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
