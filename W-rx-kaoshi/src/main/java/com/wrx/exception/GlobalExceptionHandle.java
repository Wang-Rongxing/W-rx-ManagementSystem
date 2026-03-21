package com.wrx.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wrx.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Result<Void> exceptionHandle(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialsException.class)
    public Result<Void> badCredentialsException(BadCredentialsException e) {
        e.printStackTrace();
        return Result.error("账号或密码错误");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpiredException.class, NoRolesException.class, AccessDeniedException.class})
    public Result<Void> unauthorizedExceptionHandler(Exception e) {
        e.printStackTrace();
        String msg;
        if (e instanceof TokenExpiredException) {
            msg = "登录超时，请重新登录";
        } else if (e instanceof NoRolesException) {
            msg = "没有操作权限，请登录";
        } else if (e instanceof AccessDeniedException) {
            msg = e.getMessage() + "，请重新登录";
        } else {
            msg = "未授权访问";
        }
        return Result.error(HttpStatus.UNAUTHORIZED.value(), msg);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public Result<Void> businessExceptionHandler(BusinessException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMessage());
    }
}
