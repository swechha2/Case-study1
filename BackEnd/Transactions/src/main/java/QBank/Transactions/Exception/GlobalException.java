package QBank.Transactions.Exception;

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
    public ResponseEntity<Map<String, Object>> handleGlobalExceptions(Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalTime.now());
        response.put("message", "Something went wrong");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
