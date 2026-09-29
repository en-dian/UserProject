package com.example.hedadmin.exception;

import com.example.hedadmin.response.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(BussinessException.class)
    public R hadleBussinessException(BussinessException e){
        return R.fail(e.getCode(), e.getMessage());
    }
}
