package com.ahuiali.word.pojo;

import java.io.Serializable;

/**
 * 标准单词类
 */
public class WordEct implements Serializable {

    private Integer id;

    private String word;

    private String pron_us;

    private String pron_uk;

    private String translation;

    private Integer notebook_word_id = 0;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPron_us() {
        return pron_us;
    }

    public void setPron_us(String pron_us) {
        this.pron_us = pron_us;
    }

    public String getPron_uk() {
        return pron_uk;
    }

    public void setPron_uk(String pron_uk) {
        this.pron_uk = pron_uk;
    }

    public Integer getNotebook_word_id() {
        return notebook_word_id;
    }

    public void setNotebook_word_id(Integer notebook_word_id) {
        this.notebook_word_id = notebook_word_id;
    }

    @Override
    public String toString() {
        return "WordEct{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", pron_us='" + pron_us + '\'' +
                ", pron_uk='" + pron_uk + '\'' +
                ", translation='" + translation + '\'' +
                ", notebook_id=" + notebook_word_id +
                '}';
    }
}
