package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ArticleParagraph
 * 文章内容（段） 这里用段保存是为了更好展示
 * @author ZhengChaoHui
 * @date 2020/12/12 20:59
 */
@Data
@TableName(value = "article_paragraph")
public class ArticleParagraph {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 段落号
     */
    private Integer no;

    /**
     * 内容
     */
    private String para;
}
