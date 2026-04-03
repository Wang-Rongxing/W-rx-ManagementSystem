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
 * 文章表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_article")
public class TArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属栏目 id
     */
    private Integer classId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 标题颜色
     */
    private String titleColor;

    /**
     * 标题字体大小
     */
    private String titleFontSize;

    /**
     * 标题字体样式
     */
    private Integer titleFontType;

    /**
     * 文章完整标题
     */
    private String fullTitle;

    /**
     * 文章副标题
     */
    private String subTitle;

    /**
     * 文章简介
     */
    private String intro;

    /**
     * 文章正文内容
     */
    private String content;

    /**
     * 作者 id
     */
    private Integer authorId;

    /**
     * 文章来源
     */
    private String source;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 跳转链接
     */
    private String redirectUrl;

    /**
     * 文章关键字
     */
    private String keyword;

    /**
     * 文章点击数
     */
    private Integer clickNum;

    /**
     * 文章评论数
     */
    private Integer commentNum;

    /**
     * 文章点赞数
     */
    private Integer likeNum;

    /**
     * 是否允许评论：1-允许，0-禁止
     */
    private Boolean isComment;

    /**
     * 是否置顶：1-是，0-否
     */
    private Boolean isTop;

    /**
     * 是否推荐：1-是，0-否
     */
    private Boolean isElite;

    /**
     * 是否删除：1-是，0-否
     */
    private Boolean isDeleted;

    /**
     * 文章添加时间
     */
    private LocalDateTime addTime;

    /**
     * 文章更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 文章生成时间
     */
    private LocalDateTime generateTime;

    /**
     * 最后评论时间
     */
    private LocalDateTime lastCommentTime;

    /**
     * 文章重要性标签
     */
    private String priorityTag;

    /**
     * 自定义备注
     */
    private String customRemark;

    /**
     * 文章静态页地址
     */
    private String htmlPath;

    /**
     * 附件上传地址
     */
    private String filePath;

    /**
     * 模板文件地址
     */
    private String templatePath;

    /**
     * 文章缩略图地址
     */
    private String thumbImg;

    /**
     * 静态页生成状态
     */
    private String generateStatus;

    /**
     * 文章状态标识
     */
    private Integer articleStatus;
}
