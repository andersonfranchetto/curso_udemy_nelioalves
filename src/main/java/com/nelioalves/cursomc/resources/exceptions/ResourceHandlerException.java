package com.nelioalves.cursomc.resources.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.nelioalves.cursomc.security.exceptions.AuthorizationException;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.FileException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceHandlerException {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                                              HttpStatus.NOT_FOUND.value(),
                                              "Object Not Found" ,
                                              e.getMessage(),
                                              request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                                              HttpStatus.BAD_REQUEST.value(),
                                              "Bad Request" ,
                                              e.getMessage(),
                                              request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError err = new ValidationError(System.currentTimeMillis(),
                                                  HttpStatus.UNPROCESSABLE_ENTITY.value(),
                                                  "Unprocessable Entity",
                                                  e.getMessage(),
                                                  request.getRequestURI());
        for(FieldError x : e.getBindingResult().getFieldErrors()){
            err.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorizationExeption(AuthorizationException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                                              HttpStatus.FORBIDDEN.value(),
                                              "Forbidden",
                                              e.getMessage(),
                                              request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError> fileException(FileException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Unprocessable Entity",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<StandardError> amazonServiceException(AmazonServiceException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.valueOf(e.getErrorCode()).value(),
                HttpStatus.valueOf(e.getErrorMessage()).toString(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode()).value()).body(err);
    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError> amazonClientException(AmazonClientException e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Unprocessable Entity",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<StandardError> amazonClientException(AmazonS3Exception e, HttpServletRequest request){
        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Unprocessable Entity",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
