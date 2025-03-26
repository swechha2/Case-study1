package QBank.LoginSignup.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> HandelUserNotFound(UserNotFoundException ex, WebRequest webRequest){

        Map<String, Object> body = new HashMap<>();
        body.put("time",LocalTime.now());
        body.put("message", "User not found");
        body.put("details", ex.getMessage());
        body.put("code", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> HandelInvalidCredentials(InvalidCredentialsException ex, WebRequest webRequest){
        Map<String, Object> body = new HashMap<>();
        body.put("time", LocalTime.now());
        body.put("message", "Invalid username or password");
        body.put("details", ex.getMessage());
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> HandelGlobalException(Exception ex, WebRequest webRequest){
        Map<String, Object> body = new HashMap<>();
        body.put("time", LocalTime.now());
        body.put("message", "An error occurred. Please try again");
        body.put("details", ex.getMessage());
        body.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Object> UserAlreadyExistsException(Exception ex, WebRequest webRequest){
        Map<String, Object> body = new HashMap<>();
        body.put("time", LocalTime.now());
        body.put("message", "User already exists, login to you account");
        body.put("details", ex.getMessage());
        body.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
