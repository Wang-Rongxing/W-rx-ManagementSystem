package com.wrx.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/*
*全局异常处理
* 1、底层尽量不要处理异常，尽可能把异常抛出
* 2、在程序的上层统一进行全局异常处理，让程序感知异常，并响应异常
* 3、全局异常处理：减少开销，专注业务
 */
@RestControllerAdvice //josn格式返回处理结果
public class GlobalExceptionHandle {
    //各种异常处理器：没有对应的异常处理器则使用默认的异常处理器
    //默认或统一的异常处理器
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//以http返回的状态码status（404，500，401）
    @ExceptionHandler({RuntimeException.class,Exception.class})//制定处理的异常
    public Map<String,Object> exceptionHandle(Exception e){
        e.printStackTrace();
        HashMap<String,Object> map = new HashMap<>();
        map.put("msg",e.getMessage());
        return map;
    }
    //针对各种异常的异常处理器
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String,Object> badCredentialsException(BadCredentialsException e){
        e.printStackTrace();
        HashMap<String,Object> map = new HashMap<>();
        map.put("msg","密码错误");
        return map;
    }
    //401 请求未授权异常处理（jwt 认证的异常都可以在这里一起处理）
    //返回 401，访问超时或非法，前端根据返回转态码 401，要求重新登录跳转到登录页面
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpiredException.class, NoRolesException.class, AccessDeniedException.class})
    public Map<String, Object> okenExpiredExceptionExceptionHandler(Exception e) {
        e.printStackTrace();
        String msg = null;
        if (e instanceof TokenExpiredException) {
            msg = "登录超时，请重新登录";
        } else if (e instanceof NoRolesException) {
            msg = "没有操作权限，请登录";
        } else if (e instanceof AccessDeniedException) {
            msg = e.getMessage()+ "，请重新登录";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        return map;
    }
}
