package com.example.hedadmin.exception;

import com.example.hedadmin.response.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BussinessException extends RuntimeException{

    private Integer code;
    private String message;
    public BussinessException(Integer code,String message){
        this.code=code;
    }
    public BussinessException(String message){
        this.message=message;
    }

    public BussinessException(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
}
