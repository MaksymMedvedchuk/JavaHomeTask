package com.resolution.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotThreeCommaSeparatedException.class)
	public ResponseEntity<String> handleNotThreeCommaSeparatedException(NotThreeCommaSeparatedException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(NotTenDigitsAfterDotException.class)
	public ResponseEntity<String> handleNotTenDigitsAfterDotException(NotTenDigitsAfterDotException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(NotDecimalException.class)
	public ResponseEntity<String> handleNotDecimalException(NotDecimalException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(NotRangeOfException.class)
	public ResponseEntity<String> handleNNotRangeOfException(NotRangeOfException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}

