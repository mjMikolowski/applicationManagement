package cloudservices.maciejmikolowski.ApplicationManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        JsonExceptionString jsonExceptionString = JsonExceptionString.builder()
                .errorInfo(exception.getMessage())
                .build();
        return new ResponseEntity<>(jsonExceptionString, HttpStatus.BAD_REQUEST);
    }
}
