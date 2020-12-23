package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "chapter_paragraph")
public class Paragraph implements Serializable {

    private Integer id;

    @TableField(value = "para_en")
    private String paraEn;

    @TableField(value = "para_cn")
    private String paraCn;

    @TableField(value = "chapter_index")
    private Integer chapterIndex;

    @TableField(value = "para_index")
    private Integer paraIndex;

    private Date created;

    private Date modified;

}
