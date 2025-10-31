package com.wrx.util;

import com.wrx.entity.Customer;
import com.wrx.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class LoginCustomer implements UserDetails {
    private Customer customer;
    private List<String> permissions;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //权限配置，鉴权时设置权限，把角色信息写入 GrantedAuthority
        if (permissions != null && permissions.size() > 0) {
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String rolekey : permissions) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(rolekey);
                authorities.add(simpleGrantedAuthority);
            }
            return authorities;
        } else
            return null;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {//登录名：jobId
        return customer.getCustomerId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
