package com.wrx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Getter
@Setter
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 考生号
     */
    private String sno;

    /**
     * 姓名
     */
    private String username;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证
     */
    private String idcard;

    /**
     * 科类
     */
    private String subject;

    /**
     * 特长生
     */
    private String tcs;

    /**
     * 专业名称
     */
    private String major;

    /**
     * 考试日期
     */
    private String testdate;

    /**
     * 考试时间
     */
    private String testtime;

    private String classroom;

    /**
     * 体温
     */
    private Double temperature;

    /**
     * 成绩
     */
    private Integer result;

    /**
     * 备注
     */
    private String remarks;

    private LocalDateTime committime;

    private Integer uid;
}
