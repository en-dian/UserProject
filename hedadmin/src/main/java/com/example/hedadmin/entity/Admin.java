package com.example.hedadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("admin")
@Schema(description = "管理员信息实体")
public class Admin {

    @TableId(type= IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    private String name;

    private String phone;

    private int status;

    private String email;

    private String address;


}
