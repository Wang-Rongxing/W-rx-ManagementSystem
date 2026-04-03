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
 * 资源表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_resource")
public class TResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属栏目 id
     */
    private Integer classId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源价格 (单位：分)
     */
    private Integer price;

    /**
     * 资源访问地址
     */
    private String resourceUrl;

    /**
     * 资源访问密码
     */
    private String resourcePwd;

    /**
     * 是否免费：1-是，0-否
     */
    private Boolean isFree;

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
     * 资源完整标题
     */
    private String fullTitle;

    /**
     * 资源副标题
     */
    private String subTitle;

    /**
     * 资源简介
     */
    private String intro;

    /**
     * 资源详情内容
     */
    private String content;

    /**
     * 资源作者 id
     */
    private Integer authorId;

    /**
     * 资源来源
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
     * 资源关键字
     */
    private String keyword;

    /**
     * 资源点击数
     */
    private Integer clickNum;

    /**
     * 资源评论数
     */
    private Integer commentNum;

    /**
     * 资源点赞数
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
     * 资源添加时间
     */
    private LocalDateTime addTime;

    /**
     * 资源更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 资源生成时间
     */
    private LocalDateTime generateTime;

    /**
     * 最后评论时间
     */
    private LocalDateTime lastCommentTime;

    /**
     * 资源重要性标签
     */
    private Integer priorityTag;

    /**
     * 自定义备注
     */
    private String customRemark;

    /**
     * 资源静态页地址
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
     * 资源轮播图地址
     */
    private String carouselImg;

    /**
     * 资源缩略图地址
     */
    private String thumbImg;

    /**
     * 资源视频地址
     */
    private String videoUrl;

    /**
     * 静态页生成状态
     */
    private String generateStatus;

    /**
     * 资源状态标识
     */
    private Integer resourceStatus;
}
