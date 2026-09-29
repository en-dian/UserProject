package com.example.hedadmin.response;

import lombok.Getter;

@Getter


public enum ResponseCode {
    SUCCESS(200, "操作成功！"),
    ERROR(500, "操作失败！"),
    NAME_EXIST(1001, "用户已存在，请修改手机号"),
    PARAM_ERROR(1002, "参数错误");   // 注意最后加分号

    private final Integer code;
    private final String message;

    // 枚举构造方法默认是 private，可以显式写出
    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}