package com.example.hedadmin.response;

import lombok.Data;

@Data
public class CommonResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> CommonResult<T> fail(String msg) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}