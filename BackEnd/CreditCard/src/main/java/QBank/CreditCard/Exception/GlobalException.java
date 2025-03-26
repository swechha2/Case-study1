package QBank.CreditCard.Exception;

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
        response.put("timestamp", LocalTime.now());
        response.put("message", "Something Went Wrong");
        response.put("status", ex.getMessage());
        response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handelCardNotFoundException(CardNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Card not found");
        response.put("status", ex.getMessage());
        response.put("statusCode", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFundsException(InsufficientFundsException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Not Enough Balance");
        response.put("status", ex.getMessage());
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }



}
