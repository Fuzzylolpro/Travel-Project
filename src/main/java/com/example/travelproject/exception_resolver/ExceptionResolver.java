package com.example.travelproject.exception_resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity exceptionHandlerMethod(Exception e) {
        log.warn(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AttractionsNotFound.class)
    public ResponseEntity<HttpStatus> AttractionsNotFound(Exception e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoAccessByIdExceptions.class)
    public ResponseEntity<HttpStatus> noAccessByIdException(Exception e) {
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(SameUserInDatabaseException.class)
    public ResponseEntity<HttpStatus> SameUserInDatabaseException(Exception e){
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserFromDatabaseNotFound.class)
    public ResponseEntity<HttpStatus> userFromDatabaseNotFoundException(Exception e){
        log.info(String.valueOf(e));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
