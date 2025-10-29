package com.wrx.dto;

import com.wrx.entity.SysRole;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String jobId;

    private String password;

    private String address;

    private String state;

    private String date;

    private String thumb;

    private List<SysRole> SysRoleList;//user对应的角色
}
