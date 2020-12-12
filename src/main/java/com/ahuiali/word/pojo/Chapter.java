package com.ahuiali.word.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Chapter implements Serializable {

    private Integer id;

    private String chapterName;

    private Integer bookIndex;

    private Integer chapterIndex;

    private Date created;

    private Date modified;

    private List<Paragraph> paragraphs;


}
