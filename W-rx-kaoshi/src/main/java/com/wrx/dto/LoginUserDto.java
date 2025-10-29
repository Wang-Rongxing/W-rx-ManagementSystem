package com.wrx.dto;

import lombok.Data;

import java.util.List;

/*
*登录成功返回前端的数据
 */
@Data
public class LoginUserDto {
    private String username;
    private String jobId;
    private String token;
    private List<String> roles;
}
