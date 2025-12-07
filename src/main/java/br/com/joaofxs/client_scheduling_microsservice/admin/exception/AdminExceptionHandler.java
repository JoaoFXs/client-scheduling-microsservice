package br.com.joaofxs.client_scheduling_microsservice.admin.exception;


import br.com.joaofxs.client_scheduling_microsservice.core.dto.exception.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdminExceptionHandler {


    @ExceptionHandler({BusinessNotFoundException.class})
    public ResponseEntity<ResponseException> handleBusinessNotFoundException(BusinessNotFoundException ex){
        ResponseException response = new ResponseException(HttpStatus.NOT_FOUND.name(),HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
                );
    }
}
