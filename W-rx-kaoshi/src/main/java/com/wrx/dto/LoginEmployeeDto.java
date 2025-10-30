package com.wrx.dto;

import lombok.Data;

import java.util.List;

/*
*登录成功返回前端的数据
 */
@Data
public class LoginEmployeeDto {
    private String name;
    private String employeeId;
    private String token;
    private List<String> roles;
}
