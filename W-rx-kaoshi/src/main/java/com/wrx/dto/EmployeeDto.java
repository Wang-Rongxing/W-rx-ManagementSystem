package com.wrx.dto;

import com.wrx.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {

    private Integer id;

    private String name;

    private String employeeId;

    private String password;

    private String phone;

    private List<Role> roleList;//user对应的角色
}
