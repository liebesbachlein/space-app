package app.controller;

import app.dto.response.ErrorResponse;
import app.util.exception.NonUniqueNameException;
import app.util.exception.PaymentNotProcessedException;
import app.util.exception.ReservationConflictException;
import app.util.exception.ResourceNotFoundException;
import app.util.exception.auth.InvalidOrExpiredRefreshTokenException;
import app.util.exception.auth.UserAlreadyExistsAuthenticationException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsAuthenticationException.class)
    public ResponseEntity<ErrorResponse> catchUserAlreadyExistsAuthenticationException(
            UserAlreadyExistsAuthenticationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchResourceNotFoundException(
            ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotProcessedException.class)
    public ResponseEntity<ErrorResponse> catchPaymentNotProcessedException(
            PaymentNotProcessedException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ErrorResponse> catchReservationConflictException(
            ReservationConflictException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> catchJwtException(
            JwtException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidOrExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> catchRefreshTokenException(
            InvalidOrExpiredRefreshTokenException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> catchAccessDeniedException(
            AccessDeniedException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Access denied"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NonUniqueNameException.class)
    public ResponseEntity<ErrorResponse> catchNonUniqueNameException(
            NonUniqueNameException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        String errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(". "));;
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorDetails
        );
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ConstraintViolationException ex) {
        String errorDetails = ex.getConstraintViolations()
                .stream()
                .map(e -> e.getMessage())
                .collect(Collectors.joining(". "));;
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorDetails
        );

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT);
    }
}
