package com.wrx.config;

import com.wrx.filter.JwtAuthenticationTokenFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解鉴权
public class SecurityConfig {
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    //密码明文加密方式配置
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //获取 AuthenticationManager（认证管理器），登录时认证使用
    @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception { return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 基于 token，不需要 csrf
            .csrf().disable()
            // 基于 token，不需要 session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().cors()
            //.and()
            //.exceptionHandling()
            //.authenticationEntryPoint(authenticationEntryPoint)
            //.accessDeniedHandler(accessDeniedHandlerImpl)
            .and()
            // 添加 JWT 过滤器，JWT 过滤器在用户名密码认证过滤器之前
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            // 下面开始设置权限
            .authorizeRequests(authorize -> authorize
                    // 请求放开
                    .requestMatchers("/employee/login").permitAll()
                    // .antMatchers("/**").permitAll()
                    // 其他地址的访问均需验证权限
                    .anyRequest().authenticated())
                    // 认证用户时用户信息加载配置，注入 springAuthUserService
                    // .userDetailsService(xxxAuthUserService)
            .build();
    }
}
