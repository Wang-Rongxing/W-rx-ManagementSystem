package com.wrx.util;

import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

public class LoginHelper {

    public static Authentication authenticate(AuthenticationManager authenticationManager,
                                               String rolePrefix,
                                               String username,
                                               String password) {
        String usernameWithRole = rolePrefix + username;
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(usernameWithRole, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    public static LoginUserDto buildLoginUserDto(String name,
                                                   String employeeId,
                                                   Long userId,
                                                   List<String> roles) {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setName(name);
        loginUserDto.setEmployeeId(employeeId);
        String token = JwtUtil.creatToken(userId);
        loginUserDto.setToken(token);
        loginUserDto.setRoles(roles);
        return loginUserDto;
    }

    public static List<String> convertRolesToStrings(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> roleStrings = new ArrayList<>();
        for (Role role : roles) {
            roleStrings.add(role.getRoleKey());
        }
        return roleStrings;
    }
}
