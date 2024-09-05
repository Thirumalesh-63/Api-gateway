package com.zapcom.api_gateway.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleUnauthorizedAccess(RuntimeException ex) {
		// Log the exception for debugging
		System.err.println("RuntimeException: " + ex.getMessage());

		// Return the error response with a 401 status code
		return new ResponseEntity<>(ex.getMessage().substring(5), HttpStatus.UNAUTHORIZED);
	}
}
