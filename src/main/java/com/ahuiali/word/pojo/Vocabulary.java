package com.ahuiali.word.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Created by shkstart on 2019/9/28
 */
@Data
public class Vocabulary {

    private Integer id;

    private Integer wordbookId;

    private Integer wordRank;

    private String word;

    private String paraphrase;

    private String pronUs;

    private String pronUk;

    private Date created;

    private Date modified;

}
