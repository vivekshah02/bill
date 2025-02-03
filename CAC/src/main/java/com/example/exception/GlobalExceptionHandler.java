package com.example.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;



@RestControllerAdvice
public class GlobalExceptionHandler {

  /*  @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(BillNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }*/
	
	/*@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }*/
	@ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound1(BillNotFoundException ex) {
		System.out.println("Anshita");
        ErrorResponse response = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound2(IllegalArgumentException ex) {
		System.out.println("Anshita 1");
        ErrorResponse response = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound3(NullPointerException ex) {
		System.out.println("Anshita 2");
        ErrorResponse response = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
	   System.out.println("Vini");
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

	/*@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound3(ResourceNotFoundException ex) {
		System.out.println("Anshita gupta");
        ErrorResponse response = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

	  /*  @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
	        return new ResponseEntity<>("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
	    }
*/
	   
  
    
  
}
