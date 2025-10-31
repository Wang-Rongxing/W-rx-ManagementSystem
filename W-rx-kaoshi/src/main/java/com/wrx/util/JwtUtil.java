package com.wrx.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;

import java.util.Date;
import java.util.List;

public class JwtUtil {
    public static String SING = "tomzhang";//这个尽量复杂点，且不可对外泄露
    //只把 userId 存入 jwt 负载，引入 redis 时使用
    public static String creatToken(long userId) {
        //创建 token
        return JWT.create()
                //将 userId 保存到 token 里，作为载荷
                .withClaim("userId", userId)
                .withExpiresAt(DateUtil.offsetMinute(new Date(), 360))//设置失效时间6小时后
                .sign(Algorithm.HMAC256(SING));//签名
    }
    /*
     *前后端通过 jwt 传递角色（List<String> roles）鉴权
     * 把 user 和权限转换为 toke，jwt 只支持基本数据类型，首先必须把 user 转换为 json 格式的字符
     *生成 token 把 user 也加入到负载只是把 token 弄得复杂点*/
    public static String creatToken(Employee employee, List<String> roles) {//创建 token
//
        ObjectMapper objectMapper = new ObjectMapper();//Jackson 转换 json 类
        String userStr = null;
        try { userStr = objectMapper.writeValueAsString(employee);//user 转换为 json 格式
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }return JWT.create()
                .withClaim("user", userStr)
                .withClaim("roles",roles)
                .withExpiresAt(DateUtil.offsetMinute(new Date(), 60))//设置失效时间1小时后
                .sign(Algorithm.HMAC256(SING));//签名
    }
    public static String creatToken(SysUser sysUser, List<String> roles) {//创建 token
//
        ObjectMapper objectMapper = new ObjectMapper();//Jackson 转换 json 类
        String userStr = null;
        try { userStr = objectMapper.writeValueAsString(sysUser);//user 转换为 json 格式
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }return JWT.create()
                .withClaim("user", userStr)
                .withClaim("roles",roles)
                .withExpiresAt(DateUtil.offsetMinute(new Date(), 60))//设置失效时间1小时后
                .sign(Algorithm.HMAC256(SING));//签名
    }
//    @Test
//    public void test1() throws JsonProcessingException {
//        User user = new User(); user.setId(1l);
//        user.setJobId("20190152");
//        List<String> list = new ArrayList<>(Arrays.asList("ROLE_teacher", "ROLE_edu_admin", "ROLE_sys_admin"));
//        LoginUser loginUser = new LoginUser(user, list);
//        String token = creatToken(user,list);
//        System.out.println(token);
//    }
    // 验证 token 是否被篡改，是否超时等
    public static void checkToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SING)).build();
        jwtVerifier.verify(token);
    }
}
