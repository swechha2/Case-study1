package QBank.PaymentGateWay.Exception;

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
        response.put("message", "Something went wrong");
        response.put("status", ex.getMessage());
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Something went wrong");
        response.put("status", ex.getMessage());
        response.put("code", HttpStatus.NOT_FOUND.value());
        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPasswordException(InvalidPasswordException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Something went wrong");
        response.put("status", ex.getMessage());
        response.put("code", HttpStatus.UNAUTHORIZED.value());
        return  new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
