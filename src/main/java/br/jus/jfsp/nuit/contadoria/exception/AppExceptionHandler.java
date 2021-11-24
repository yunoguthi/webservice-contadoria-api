package br.jus.jfsp.nuit.contadoria.exception;

import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RestController
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ExceptionResponse> recordNotFound(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .date(LocalDateTime.now())
                        .message(exception.getMessage())
                        .status(NOT_FOUND.value())
                        .build(),
                NOT_FOUND);
    }

    @ExceptionHandler(StaleObjectStateException.class)
    public ResponseEntity<ExceptionResponse> optimistickLock(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .date(LocalDateTime.now())
                        .message("Registro alterado por outro usuário")
                        .detail(exception.getMessage())
                        .status(INTERNAL_SERVER_ERROR.value())
                        .build(),
                INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            EntityExistsException.class,
            IllegalArgumentException.class,
            TransactionRequiredException.class,
            PersistenceException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ExceptionResponse> internalServerError(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .date(LocalDateTime.now())
                        .message(exception.getMessage())
                        .status(INTERNAL_SERVER_ERROR.value())
                        .build(),
                INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .date(LocalDateTime.now())
                        .message("Trocando os metodos HTTP? " + ex.getMessage())
                        .status(METHOD_NOT_ALLOWED.value())
                        .build(),
                METHOD_NOT_ALLOWED);
    }
}
