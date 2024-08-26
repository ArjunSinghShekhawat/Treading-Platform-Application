package com.treadingPlatformApplication.exception;

import com.treadingPlatformApplication.responce.ResponceApi;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Data
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponceApi> resourceNotFoundExceptionHandler(ResourceNotFoundException rs){

        String message  = rs.getMessage();

        ResponceApi responceApi = new ResponceApi();
        responceApi.setMessage(message);
        responceApi.setStatus(true);

        return  new ResponseEntity<>(responceApi, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ms){
        Map<String,String>repo = new HashMap<>();

        ms.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String defaultMessage = error.getDefaultMessage();

            repo.put(fieldName,defaultMessage);
        });
        return new ResponseEntity<>(repo,HttpStatus.OK);
    }
}
