package com.ahuiali.word.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Paragraph implements Serializable {

    private Integer id;

    private String paraEn;

    private String para_cn;

    private Integer chapter_index;

    private Integer para_index;

    private Date created;

    private Date modified;

}
