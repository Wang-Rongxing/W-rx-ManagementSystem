package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资源评论表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_resource_comment")
public class TResourceComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论用户名
     */
    private String userName;

    /**
     * 评论人邮箱
     */
    private String email;

    /**
     * 评论创建时间
     */
    private LocalDateTime createTime;

    /**
     * 父评论 id，0 为根评论
     */
    private Integer parentId;

    /**
     * 外部关联 id
     */
    private Integer foreignId;

    /**
     * 评论人头像
     */
    private String avatar;

    /**
     * 关联资源 id
     */
    private Integer resourceId;

    /**
     * 评论人用户 id
     */
    private Integer userId;
}
