package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName(value = "book")
public class Book implements Serializable {

    private Integer id;

    private String title;

    private String img;

    private String author;

    private String tag;

    //是否推荐，0否1是
    @TableField(value = "id_hot")
    private Integer isHot;

    private String summary;

    @TableField(value = "index_book")
    private Integer indexBook;

    @TableField(value = "lastest_loc")
    private String lastestLoc;

    private List<Chapter> chapters;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", author='" + author + '\'' +
                ", tag='" + tag + '\'' +
                ", isHot=" + isHot +
                ", summary='" + summary + '\'' +
                ", indexBook=" + indexBook +
                ", lastestLoc='" + lastestLoc + '\'' +
                ", chapters=" + chapters +
                '}';
    }
}
