package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文章分类表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_article_class")
public class TArticleClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String className;

    /**
     * 父级分类 id，0 为顶级分类
     */
    private Integer parentId;

    /**
     * 分类别名
     */
    private String classAlias;

    /**
     * 分类描述
     */
    private String classDesc;

    /**
     * 分类封面图
     */
    private String classImg;

    /**
     * 是否置顶：1-是，0-否
     */
    private Boolean isTop;

    /**
     * 分类下文章数量
     */
    private Integer articleCount;
}
