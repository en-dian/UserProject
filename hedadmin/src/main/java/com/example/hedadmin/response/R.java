package com.example.hedadmin.response;

import lombok.Getter;

@Getter
public class R<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 泛型类型的数据，自定义类型
     */
    private T data;
    private R(Integer code) {
        this.code = code;
    }
    private R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success()
    {
        return new R<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> R<T> success(String message){
        return new R<>(ResponseCode.SUCCESS.getCode(), message);
    }
    public static <T> R<T> data(T data){
        return new R<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),data);
    }
    public static <T> R<T> fail()
    {
        return new R<>(ResponseCode.ERROR.getCode());
    }

    public static <T> R<T> fail(String message){
        return new R<>(ResponseCode.ERROR.getCode(), message);
    }
    public static <T> R<T> fail(Integer code,String message){
        return new R<>(code, message);
    }
}
