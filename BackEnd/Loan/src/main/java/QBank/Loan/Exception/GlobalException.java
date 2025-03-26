package QBank.Loan.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "something went wrong");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoLoanFound.class)
    public ResponseEntity<Map<String, Object>> handleNoLoanFoundException(NoLoanFound ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Loan not found");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalanceException(InsufficientBalanceException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Insufficient Balance");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

}
