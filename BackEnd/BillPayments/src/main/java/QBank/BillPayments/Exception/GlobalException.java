package QBank.BillPayments.Exception;

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
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestapm", LocalTime.now());
        response.put("message", ex.getMessage());
        response.put("status", "Internal Server Error");
        response.put("statuscode", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handelBillNotFoundException(BillNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestapm", LocalTime.now());
        response.put("message", ex.getMessage());
        response.put("status", "Bill not found");
        response.put("statuscode", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientFundsExceptions.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFundsException(InsufficientFundsExceptions ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestapm", LocalTime.now());
        response.put("message", ex.getMessage());
        response.put("status", "Balance not enough");
        response.put("statuscode", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
